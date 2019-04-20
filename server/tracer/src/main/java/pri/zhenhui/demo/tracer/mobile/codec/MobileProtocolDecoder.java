package pri.zhenhui.demo.tracer.mobile.codec;

import io.netty.channel.ChannelHandlerContext;
import pri.zhenhui.demo.tracer.domain.Message;
import pri.zhenhui.demo.tracer.support.codec.AbstractProtocolDecoder;

import java.util.List;

public class MobileProtocolDecoder extends AbstractProtocolDecoder<Message> {

    @Override
    public List<Message> decode(ChannelHandlerContext ctx, Object msg) throws Exception {
        return null;
    }

}













