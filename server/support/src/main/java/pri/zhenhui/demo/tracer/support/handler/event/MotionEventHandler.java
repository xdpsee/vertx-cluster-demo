/*
 * Copyright 2016 Anton Tananaev (anton@traccar.org)
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
import pri.zhenhui.demo.tracer.domain.Configs;
import pri.zhenhui.demo.tracer.domain.Event;
import pri.zhenhui.demo.tracer.domain.Position;
import pri.zhenhui.demo.tracer.domain.UniqueID;
import pri.zhenhui.demo.tracer.enums.EventType;
import pri.zhenhui.demo.tracer.server.ServerConnector;
import pri.zhenhui.demo.tracer.support.handler.AbstractEventHandler;

import java.util.ArrayList;
import java.util.List;

public class MotionEventHandler extends AbstractEventHandler {

    private double speedThreshold;

    public MotionEventHandler(ServerConnector connector) {
        super(connector);
        speedThreshold = configs().getDouble(Configs.EVENT_MOTION_SPEED_THRESHOLD, 0.01);
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

                    final List<Event> events = new ArrayList<>();

                    Position lastPosition = queryLastPos.result();
                    double speed = position.getSpeed(), oldSpeed = 0;
                    if (lastPosition != null) {
                        oldSpeed = lastPosition.getSpeed();
                    }

                    if (speed > speedThreshold && oldSpeed <= speedThreshold) {
                        events.add(new Event(EventType.TYPE_DEVICE_MOVING, position));
                    } else if (speed <= speedThreshold && oldSpeed > speedThreshold) {
                        events.add(new Event(EventType.TYPE_DEVICE_STOPPED, position));
                    }

                    resultHandler.handle(Future.succeededFuture(events));
                });

            });
        } catch (Throwable e) {
            resultHandler.handle(Future.failedFuture(e));
        }

    }

}
