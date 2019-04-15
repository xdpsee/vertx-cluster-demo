package pri.zhenhui.demo.tracer.support.handler;

import io.netty.channel.ChannelHandlerContext;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import pri.zhenhui.demo.tracer.domain.Device;
import pri.zhenhui.demo.tracer.domain.Position;
import pri.zhenhui.demo.tracer.domain.UniqueID;
import pri.zhenhui.demo.tracer.server.Connection;
import pri.zhenhui.demo.tracer.server.ServerConnector;
import pri.zhenhui.demo.tracer.service.DeviceReadService;
import pri.zhenhui.demo.tracer.service.PositionReadService;

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

    protected void queryDevice(UniqueID deviceId, Handler<AsyncResult<Device>> resultHandler) {

        try {
            DeviceReadService deviceReadService = connector.context().getService(DeviceReadService.SERVICE_NAME, DeviceReadService.SERVICE_ADDRESS, DeviceReadService.class);
            deviceReadService.queryDevice(deviceId, resultHandler);
        } catch (Throwable e) {
            resultHandler.handle(Future.failedFuture(e));
        }
    }

    protected void lastPosition(UniqueID deviceId, Handler<AsyncResult<Position>> resultHandler) {

        try {
            PositionReadService positionReadService = connector.context().getService(PositionReadService.SERVICE_NAME, PositionReadService.SERVICE_ADDRESS, PositionReadService.class);
            positionReadService.queryLastPosition(deviceId, resultHandler);
        } catch (Throwable e) {
            resultHandler.handle(Future.failedFuture(e));
        }
    }
}
