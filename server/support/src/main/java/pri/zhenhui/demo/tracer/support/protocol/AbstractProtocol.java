package pri.zhenhui.demo.tracer.support.protocol;

import io.netty.handler.codec.string.StringEncoder;
import pri.zhenhui.demo.tracer.server.Connection;
import pri.zhenhui.demo.tracer.server.Protocol;
import pri.zhenhui.demo.tracer.domain.Command;
import pri.zhenhui.demo.tracer.enums.CommandType;
import pri.zhenhui.demo.tracer.support.server.ServerContext;
import pri.zhenhui.demo.tracer.service.DeviceReadService;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractProtocol implements Protocol {

    private final ServerContext context;
    private final String name;

    protected AbstractProtocol(ServerContext context, String name) {
        this.context = context;
        this.name = name;
    }

    @Override
    public final String getName() {
        return this.name;
    }

    @Override
    public Collection<CommandType> supportedCommands() {
        Set<CommandType> commands = new HashSet<>();

        commands.add(CommandType.TYPE_CUSTOM);

        return commands;
    }

    @Override
    public void sendCommand(Connection connection, Command command) {

        DeviceReadService deviceReadService = context.getService(DeviceReadService.SERVICE_NAME, DeviceReadService.SERVICE_ADDRESS, DeviceReadService.class);

        deviceReadService.supportedCommands(connection.deviceId(), query -> {
            if (query.failed()) {
                throw new RuntimeException(query.cause());
            } else {
                Set<String> supportedCommands = query.result();
                if (supportedCommands.contains(command.getType().name())) {
                    connection.write(command);
                } else if (command.getType().equals(CommandType.TYPE_CUSTOM)) {
                    String data = command.getString(Command.KEY_DATA);
                    if (connection.channel().pipeline().get(StringEncoder.class) != null) {
                        connection.write(data);
                    } else {
                        connection.write(command);
                    }
                } else {
                    throw new RuntimeException("Command " + command.getType() + " is not supported in protocol " + getName());
                }
            }
        });
    }

}
