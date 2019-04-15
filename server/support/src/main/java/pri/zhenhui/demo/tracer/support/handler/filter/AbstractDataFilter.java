package pri.zhenhui.demo.tracer.support.handler.filter;


import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import pri.zhenhui.demo.tracer.support.handler.AbstractDataHandler;
import pri.zhenhui.demo.tracer.domain.Position;
import pri.zhenhui.demo.tracer.server.ServerConnector;

public abstract class AbstractDataFilter extends AbstractDataHandler {

    private static final Logger logger = LoggerFactory.getLogger(AbstractDataFilter.class);

    private final FilterPolicy filterPolicy;

    protected AbstractDataFilter(ServerConnector connector, FilterPolicy filterPolicy) {
        super(connector);
        this.filterPolicy = filterPolicy;
    }

    @Override
    protected void handlePosition(Position position, Handler<AsyncResult<Position>> resultHandler) {

        connector.context().executeBlocking(future -> {

            try {
                if (filterPolicy != null) {
                    positionReadService().queryLastPosition(position.deviceId(), queryLastPosition -> {
                        if (queryLastPosition.failed()) {
                            future.fail(queryLastPosition.cause());
                        } else {
                            try {
                                if (!filterPolicy.accept(position, queryLastPosition.result())) {
                                    future.complete(null);
                                } else {
                                    future.complete(position);
                                }
                            } catch (Exception e) {
                                future.fail(e);
                            }
                        }
                    });
                } else {
                    future.complete(position);
                }

            } catch (Exception e) {
                future.fail(e);
            }

        }, resultHandler);
    }

}
