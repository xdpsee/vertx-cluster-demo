package pri.zhenhui.demo.tracer.support.handler;


import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import pri.zhenhui.demo.tracer.domain.Position;
import pri.zhenhui.demo.tracer.server.ServerConnector;

public class DefaultDataHandler extends AbstractDataHandler {

    private static final Logger logger = LoggerFactory.getLogger(DefaultDataHandler.class);

    public DefaultDataHandler(ServerConnector connector) {
        super(connector);
    }

    @Override
    protected void handlePosition(Position position, Handler<AsyncResult<Position>> resultHandler) {

        positionWriteService().savePosition(position, savePosition -> {
            if (savePosition.succeeded()) {
                resultHandler.handle(Future.succeededFuture(position));
            } else {
                resultHandler.handle(Future.failedFuture(savePosition.cause()));
            }
        });

    }

}
