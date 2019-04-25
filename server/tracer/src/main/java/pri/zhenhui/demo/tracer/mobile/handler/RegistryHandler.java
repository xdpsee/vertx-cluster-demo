package pri.zhenhui.demo.tracer.mobile.handler;

import io.netty.channel.ChannelHandlerContext;
import pri.zhenhui.demo.tracer.mobile.message.RegistryMessage;
import pri.zhenhui.demo.tracer.server.Connection;
import pri.zhenhui.demo.tracer.server.ServerConnector;
import pri.zhenhui.demo.tracer.support.handler.AbstractHandler;

public class RegistryHandler extends AbstractHandler<RegistryMessage> {

    public RegistryHandler(ServerConnector connector) {
        super(connector);
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Connection connection, RegistryMessage msg) throws Exception {

        connection.write("HAHAHAHA");

    }
}
