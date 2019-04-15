/*
 * Copyright 2016 Anton Tananaev (anton@traccar.org)
 * Copyright 2016 Andrey Kunitsyn (andrey@traccar.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pri.zhenhui.demo.tracer.support.handler.event;


import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import pri.zhenhui.demo.tracer.domain.Event;
import pri.zhenhui.demo.tracer.domain.Position;
import pri.zhenhui.demo.tracer.domain.UniqueID;
import pri.zhenhui.demo.tracer.enums.EventType;
import pri.zhenhui.demo.tracer.server.ServerConnector;
import pri.zhenhui.demo.tracer.support.handler.AbstractEventHandler;

import java.util.ArrayList;
import java.util.List;

public class IgnitionEventHandler extends AbstractEventHandler {

    public IgnitionEventHandler(ServerConnector connector) {
        super(connector);
    }

    @Override
    protected void analyzeEvent(Position position, Handler<AsyncResult<List<Event>>> resultHandler) {

        final UniqueID deviceId = position.deviceId();

        try {
            deviceReadService().queryDevice(deviceId, queryDevice -> {
                if (queryDevice.failed()) {
                    resultHandler.handle(Future.failedFuture(queryDevice.cause()));
                    return;
                }

                positionReadService().queryLastPosition(deviceId, queryLastPos -> {
                    if (queryLastPos.failed()) {
                        resultHandler.handle(Future.failedFuture(queryDevice.cause()));
                        return;
                    }

                    Position lastPos = queryLastPos.result();
                    boolean ignition = position.getBoolean(Position.KEY_IGNITION);
                    final List<Event> events = new ArrayList<>();

                    if (lastPos != null && lastPos.getId().equals(position.getId()) && lastPos.hasKey(Position.KEY_IGNITION)) {
                        boolean oldIgnition = lastPos.getBoolean(Position.KEY_IGNITION);
                        if (ignition && !oldIgnition) {
                            events.add(new Event(EventType.TYPE_IGNITION_ON, deviceId, position.getId()));
                        } else if (!ignition && oldIgnition) {
                            events.add(new Event(EventType.TYPE_IGNITION_OFF, deviceId, position.getId()));
                        }
                    }

                    resultHandler.handle(Future.succeededFuture(events));
                });

            });
        } catch (Throwable e) {
            resultHandler.handle(Future.failedFuture(e));
        }
    }
}


