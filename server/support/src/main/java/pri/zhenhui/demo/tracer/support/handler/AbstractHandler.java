package pri.zhenhui.demo.tracer.support.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import pri.zhenhui.demo.tracer.domain.Configs;
import pri.zhenhui.demo.tracer.domain.Device;
import pri.zhenhui.demo.tracer.domain.Message;
import pri.zhenhui.demo.tracer.domain.UniqueID;
import pri.zhenhui.demo.tracer.server.Connection;
import pri.zhenhui.demo.tracer.server.ServerConnector;
import pri.zhenhui.demo.tracer.service.DeviceReadService;
import pri.zhenhui.demo.tracer.service.EventWriteService;
import pri.zhenhui.demo.tracer.service.PositionReadService;
import pri.zhenhui.demo.tracer.service.PositionWriteService;
import pri.zhenhui.demo.tracer.support.exception.DeviceException;
import pri.zhenhui.demo.tracer.utils.ChannelAttribute;
import pri.zhenhui.demo.tracer.utils.ChannelAttributesUtils;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;

public abstract class AbstractHandler<T extends Message> extends SimpleChannelInboundHandler<T> implements io.netty.channel.ChannelInboundHandler {

    private static final Logger logger = LoggerFactory.getLogger(AbstractHandler.class);

    protected final ServerConnector connector;

    public AbstractHandler(ServerConnector connector) {
        super();
        this.connector = connector;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, T msg) throws Exception {

        checkDevice(ctx, msg, checkDevice -> {
            if (checkDevice.failed()) {
                ctx.close();
            } else {
                try {
                    Connection connection = (Connection) ChannelAttributesUtils.get(ctx.channel(), ChannelAttribute.CONNECTION);
                    messageReceived(ctx, connection, msg);
                } catch (Exception e) {
                    ctx.close();
                }
            }
        });
    }

    protected abstract void messageReceived(ChannelHandlerContext ctx, Connection connection, T msg) throws Exception;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        final Channel channel = ctx.channel();
        if (cause instanceof ClosedChannelException) {
            logger.warn("AbstractHandler.exceptionCaught, attempt to write to closed channel " + channel);
        } else {
            if (cause instanceof IOException && "Connection reset by peer".equals(cause.getMessage())) {
                logger.warn("AbstractHandler.exceptionCaught, connection reset by peer");
            } else {
                logger.error("AbstractHandler.exceptionCaught, unexpected exception from downstream for " + channel,
                        cause);
            }

            ctx.close();
        }
    }

    protected final Configs configs() {
        return connector.configs();
    }

    private void checkDevice(ChannelHandlerContext ctx, T msg, Handler<AsyncResult<Void>> resultHandler) throws Exception {

        connector.context().executeBlocking(future -> {
            if (null == ChannelAttributesUtils.get(ctx, ChannelAttribute.DEVICE_ID)) {
                final UniqueID deviceId = msg.deviceId();
                if (deviceId != null) {
                    deviceReadService().queryDevice(deviceId, queryDevice -> {
                        if (queryDevice.failed()) {
                            future.fail(queryDevice.cause());
                        } else {
                            Device device = queryDevice.result();
                            if (device != null) {
                                ChannelAttributesUtils.setIfAbsent(ctx, ChannelAttribute.DEVICE_ID, device.getId());
                                future.complete();
                            } else {
                                future.fail(new DeviceException(String.format("device: %s not found", deviceId)));
                            }
                        }
                    });
                }
            } else {
                future.complete();
            }
        }, resultHandler);
    }

    protected DeviceReadService deviceReadService() {
        return connector.context().getService(DeviceReadService.SERVICE_NAME, DeviceReadService.SERVICE_ADDRESS, DeviceReadService.class);
    }

    protected EventWriteService eventWriteService() {
        return connector.context().getService(EventWriteService.SERVICE_NAME, EventWriteService.SERVICE_ADDRESS, EventWriteService.class);
    }

    protected PositionReadService positionReadService() {
        return connector.context().getService(PositionReadService.SERVICE_NAME, PositionReadService.SERVICE_ADDRESS, PositionReadService.class);
    }

    protected PositionWriteService positionWriteService() {
        return connector.context().getService(PositionWriteService.SERVICE_NAME, PositionWriteService.SERVICE_ADDRESS, PositionWriteService.class);
    }


}

