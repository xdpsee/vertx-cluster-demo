package pri.zhenhui.demo.tracer.server;

import pri.zhenhui.demo.tracer.domain.Command;
import pri.zhenhui.demo.tracer.enums.CommandType;

import java.util.Collection;
import java.util.Set;

public interface Protocol {

    String getName();

    Set<CommandType> supportedCommands();

    void sendCommand(Connection connection, Command command);

}
