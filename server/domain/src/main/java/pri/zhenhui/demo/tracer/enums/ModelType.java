package pri.zhenhui.demo.tracer.enums;

import java.util.Arrays;
import java.util.List;

public enum ModelType {

    MOBILE(1, "mobile", Arrays.asList(CommandType.TYPE_CUSTOM)),
    ;

    public final long id;

    public final String protocol;

    public final List<CommandType> supportedCommands;

    ModelType(long id, String protocol, List<CommandType> supportedCommands) {
        this.id = id;
        this.protocol = protocol;
        this.supportedCommands = supportedCommands;
    }
}


