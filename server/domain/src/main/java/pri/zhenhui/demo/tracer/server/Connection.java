package pri.zhenhui.demo.tracer.server;

import io.netty.channel.Channel;
import pri.zhenhui.demo.tracer.domain.Command;
import pri.zhenhui.demo.tracer.domain.UniqueID;

public interface Connection {

    UniqueID deviceId();

    Channel channel();

    Protocol protocol();

    void send(Command command);

    void write(Object message);
}


