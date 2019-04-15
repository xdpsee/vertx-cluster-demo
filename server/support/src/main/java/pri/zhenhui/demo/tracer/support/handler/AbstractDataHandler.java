package pri.zhenhui.demo.tracer.support.handler;

import io.netty.channel.ChannelHandlerContext;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import pri.zhenhui.demo.tracer.domain.Position;
import pri.zhenhui.demo.tracer.server.Connection;
import pri.zhenhui.demo.tracer.server.ServerConnector;

public abstract class AbstractDataHandler extends AbstractHandler<Position> {

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
                ctx.close();
            }
        });
    }

    protected abstract void handlePosition(Position position, Handler<AsyncResult<Position>> resultHandler);
}
