package pri.zhenhui.demo.tracer.mobile.codec;

import io.netty.channel.ChannelHandlerContext;
import pri.zhenhui.demo.tracer.domain.Message;
import pri.zhenhui.demo.tracer.mobile.message.RegistryMessage;
import pri.zhenhui.demo.tracer.support.codec.AbstractProtocolDecoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MobileProtocolDecoder extends AbstractProtocolDecoder<String, Message> {

    @Override
    public List<Message> decode(ChannelHandlerContext ctx, String message) throws Exception {

        if (message.startsWith("##1,")) {
            return Collections.singletonList(new RegistryMessage(message));
        }

        if (message.startsWith("##2,")) {

        }

        return new ArrayList<>();
    }

}






