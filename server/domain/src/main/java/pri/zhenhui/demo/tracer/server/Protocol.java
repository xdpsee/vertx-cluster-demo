package pri.zhenhui.demo.tracer.server;

import pri.zhenhui.demo.tracer.domain.Command;
import pri.zhenhui.demo.tracer.enums.CommandType;

import java.util.Collection;

public interface Protocol {

    String getName();

    Collection<CommandType> supportedCommands();

    void sendCommand(Connection connection, Command command);

}
