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
import pri.zhenhui.demo.tracer.domain.*;
import pri.zhenhui.demo.tracer.enums.EventType;
import pri.zhenhui.demo.tracer.server.ServerConnector;
import pri.zhenhui.demo.tracer.support.handler.AbstractEventHandler;

import java.util.ArrayList;
import java.util.List;

public class OverspeedEventHandler extends AbstractEventHandler {

    private final boolean notRepeat;

    public OverspeedEventHandler(ServerConnector connector) {
        super(connector);
        notRepeat = configs().getBoolean(Configs.EVENT_OVERSPEED_NOT_REPEAT, false);
    }

    @Override
    protected void analyzeEvent(Position currPos, Handler<AsyncResult<List<Event>>> resultHandler) {
        final UniqueID deviceId = currPos.deviceId();

        try {
            deviceReadService().queryDevice(deviceId, queryDevice -> {
                if (queryDevice.failed()) {
                    resultHandler.handle(Future.failedFuture(queryDevice.cause()));
                    return;
                }

                final Device device = queryDevice.result();

                positionReadService().queryLastPosition(deviceId, queryLastPos -> {
                    if (queryLastPos.failed()) {
                        resultHandler.handle(Future.failedFuture(queryLastPos.cause()));
                        return;
                    }

                    final List<Event> events = new ArrayList<>();

                    double speed = currPos.getSpeed();
                    double speedLimit = device.getDouble(Device.KEY_SPEED_LIMIT);
                    if (speedLimit > 0 && currPos.isLocated()) {
                        final Position lastPos = queryLastPos.result();
                        double oldSpeed = 0;
                        if (notRepeat) {
                            if (lastPos != null) {
                                oldSpeed = lastPos.getSpeed();
                            }
                        }

                        if (lastPos == null
                                || (!lastPos.getId().equals(currPos.getId())
                                && speed > speedLimit
                                && oldSpeed <= speedLimit)
                        ) {
                            Event event = new Event(EventType.TYPE_DEVICE_OVERSPEED, currPos.getDeviceId(), currPos.getId());
                            event.set(Event.SPEED, speed);
                            event.set(Device.KEY_SPEED_LIMIT, speedLimit);
                            events.add(event);
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



