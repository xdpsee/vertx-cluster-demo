package pri.zhenhui.demo.tracer.support.handler;


import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import pri.zhenhui.demo.tracer.domain.Event;
import pri.zhenhui.demo.tracer.domain.Position;
import pri.zhenhui.demo.tracer.server.ServerConnector;

import java.util.List;

public abstract class AbstractEventHandler extends AbstractDataHandler {

    public AbstractEventHandler(ServerConnector connector) {
        super(connector);
    }

    @Override
    protected void handlePosition(Position position, Handler<AsyncResult<Position>> resultHandler) {

        analyzeEvent(position, analyzeEvent -> {
            // analyze event 不影响 现有position
            resultHandler.handle(Future.succeededFuture(position));

            if (analyzeEvent.failed()) {
                analyzeEvent.cause().printStackTrace();
            } else {
                eventWriteService().saveEvent(analyzeEvent.result(), saveEvent -> {
                    if (saveEvent.failed()) {
                        saveEvent.cause().printStackTrace();
                    }
                });
            }
        });
    }

    protected abstract void analyzeEvent(Position position, Handler<AsyncResult<List<Event>>> resultHandler);

}


