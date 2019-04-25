package pri.zhenhui.demo.tracer.support.handler;

import io.netty.channel.ChannelHandlerContext;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import pri.zhenhui.demo.tracer.domain.Position;
import pri.zhenhui.demo.tracer.server.Connection;
import pri.zhenhui.demo.tracer.server.ServerConnector;

public abstract class AbstractDataHandler extends AbstractHandler<Position> {

    private static final Logger logger = LoggerFactory.getLogger(DefaultDataHandler.class);

    public AbstractDataHandler(ServerConnector connector) {
        super(connector);
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Connection connection, Position position) throws Exception {

        handlePosition(position, handlePosition -> {
            if (handlePosition.succeeded() && handlePosition.result() != null) {
                ctx.fireChannelRead(handlePosition.result());
                return;
            }

            if (handlePosition.failed()) {
                logger.error("AbstractDataHandler.handlePosition exception.", handlePosition.cause());
                ctx.close();
            }
        });
    }

    protected abstract void handlePosition(Position position, Handler<AsyncResult<Position>> resultHandler);
}
