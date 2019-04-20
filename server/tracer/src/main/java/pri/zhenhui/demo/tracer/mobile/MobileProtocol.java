package pri.zhenhui.demo.tracer.mobile;

import pri.zhenhui.demo.tracer.enums.CommandType;
import pri.zhenhui.demo.tracer.support.protocol.AbstractProtocol;

import java.util.HashSet;
import java.util.Set;

public class MobileProtocol extends AbstractProtocol {

    public MobileProtocol() {
        super("mobile");
    }

    @Override
    public Set<CommandType> supportedCommands() {
        return new HashSet<>();
    }
}
