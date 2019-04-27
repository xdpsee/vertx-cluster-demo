package pri.zhenhui.demo.tracer.mobile.handler;

import io.netty.channel.ChannelHandlerContext;
import pri.zhenhui.demo.tracer.domain.Command;
import pri.zhenhui.demo.tracer.enums.CommandType;
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

        Command command = new Command();
        command.setType(CommandType.TYPE_CUSTOM);
        command.setDeviceId(msg.deviceId());
        command.set(Command.KEY_DATA, "OK");

        connection.send(command);

    }
}
