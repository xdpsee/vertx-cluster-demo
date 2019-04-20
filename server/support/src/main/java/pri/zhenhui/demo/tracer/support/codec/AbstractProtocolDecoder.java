package pri.zhenhui.demo.tracer.support.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import pri.zhenhui.demo.tracer.domain.Message;
import pri.zhenhui.demo.tracer.domain.Position;
import pri.zhenhui.demo.tracer.server.Decoder;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.util.List;

public abstract class AbstractProtocolDecoder<T extends Message> extends MessageToMessageDecoder<T> implements Decoder<T> {

    @Override
    protected final void decode(ChannelHandlerContext ctx, T msg, List<Object> out) throws Exception {

        final List<T> results = decode(ctx, msg);

        results.forEach(result -> {
            if (result == msg) {
                ctx.fireChannelRead(result);
            } else {
                saveOrigin(result, msg);
                out.add(result);
            }
        });
    }

    private void saveOrigin(Object decodedMessage, Object originalMessage) {

        if (decodedMessage instanceof Position) {
            Position position = (Position) decodedMessage;
            if (originalMessage instanceof ByteBuf) {
                ByteBuf buf = (ByteBuf) originalMessage;
                position.set(Position.KEY_ORIGINAL, ByteBufUtil.hexDump(buf, 0, buf.writerIndex()));
            } else if (originalMessage instanceof String) {
                position.set(Position.KEY_ORIGINAL, DatatypeConverter.printHexBinary(
                        ((String) originalMessage).getBytes(StandardCharsets.UTF_8)));
            }

        }
    }
}




