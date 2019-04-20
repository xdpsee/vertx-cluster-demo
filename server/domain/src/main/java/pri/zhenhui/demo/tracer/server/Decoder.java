package pri.zhenhui.demo.tracer.server;

import io.netty.channel.ChannelHandlerContext;
import pri.zhenhui.demo.tracer.domain.Message;

import java.util.List;

public interface Decoder<T extends Message>  {

    List<T> decode(ChannelHandlerContext ctx, Object msg) throws Exception;

}



