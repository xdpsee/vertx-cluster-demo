package pri.zhenhui.demo.tracer.support.protocol;

import io.netty.handler.codec.string.StringEncoder;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import pri.zhenhui.demo.tracer.domain.Command;
import pri.zhenhui.demo.tracer.enums.CommandType;
import pri.zhenhui.demo.tracer.server.Connection;
import pri.zhenhui.demo.tracer.server.Protocol;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractProtocol implements Protocol {

    private static final Logger logger = LoggerFactory.getLogger(AbstractProtocol.class);

    private final String name;

    protected AbstractProtocol(String name) {
        this.name = name;
    }

    @Override
    public final String getName() {
        return this.name;
    }

    @Override
    public Set<CommandType> supportedCommands() {
        Set<CommandType> commands = new HashSet<>();

        commands.add(CommandType.TYPE_CUSTOM);

        return commands;
    }

    @Override
    public void sendCommand(Connection connection, Command command) {

        final Set<CommandType> supportedCommands = connection.protocol().supportedCommands();
        if (supportedCommands.contains(command.getType())) {
            connection.write(command);
        } else if (command.getType().equals(CommandType.TYPE_CUSTOM)) {
            try {
                String data = command.getString(Command.KEY_DATA);
                if (connection.channel().pipeline().get(StringEncoder.class) != null) {
                    connection.write(data);
                } else {
                    connection.write(command);
                }
            } catch (Exception e) {
                logger.error("sendCommand exception", e);
            }
        } else {
            throw new IllegalArgumentException("Command " + command.getType() + " is not supported in protocol " + getName());
        }
    }

}
