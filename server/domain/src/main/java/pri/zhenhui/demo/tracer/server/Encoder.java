package pri.zhenhui.demo.tracer.server;

import io.netty.handler.codec.MessageToByteEncoder;
import pri.zhenhui.demo.tracer.domain.Command;

public abstract class Encoder extends MessageToByteEncoder<Command> {

}


