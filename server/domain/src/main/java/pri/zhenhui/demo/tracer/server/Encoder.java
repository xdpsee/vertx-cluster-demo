package pri.zhenhui.demo.tracer.server;

import pri.zhenhui.demo.tracer.domain.Command;

public interface Encoder {

    byte[] encode(Command command) throws Exception;

}


