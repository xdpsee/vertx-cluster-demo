package pri.zhenhui.demo.tracer.mobile;

import pri.zhenhui.demo.tracer.enums.CommandType;
import pri.zhenhui.demo.tracer.support.protocol.AbstractProtocol;
import pri.zhenhui.demo.tracer.support.server.ServerContext;

import java.util.HashSet;
import java.util.Set;

public class MobileProtocol extends AbstractProtocol {

    public MobileProtocol(ServerContext context) {
        super(context, "mobile");
    }

    @Override
    public Set<CommandType> supportedCommands() {
        return new HashSet<>();
    }
}
