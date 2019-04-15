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
import io.vertx.core.Handler;
import pri.zhenhui.demo.tracer.domain.Event;
import pri.zhenhui.demo.tracer.domain.Position;
import pri.zhenhui.demo.tracer.enums.EventType;
import pri.zhenhui.demo.tracer.server.ServerConnector;
import pri.zhenhui.demo.tracer.support.handler.AbstractEventHandler;

import java.util.ArrayList;
import java.util.List;

public class CommandResultEventHandler extends AbstractEventHandler {

    public CommandResultEventHandler(ServerConnector connector) {
        super(connector);
    }

    @Override
    protected void analyzeEvent(Position position, Handler<AsyncResult<List<Event>>> resultHandler) {
        connector.context().executeBlocking(future -> {
            try {
                final List<Event> events = new ArrayList<>();

                String commandResult = position.getString(Position.KEY_RESULT);
                if (commandResult != null) {
                    Event event = new Event(EventType.TYPE_COMMAND_RESULT, position.getDeviceId(), position.getId());
                    event.set(Position.KEY_RESULT, commandResult);
                    events.add(event);
                }
                future.complete(events);
            } catch (Exception e) {
                future.fail(e);
            }
        }, resultHandler);

    }

}
