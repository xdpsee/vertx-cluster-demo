package pri.zhenhui.demo.tracer.support.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import pri.zhenhui.demo.tracer.domain.Command;
import pri.zhenhui.demo.tracer.server.Encoder;

public abstract class AbstractProtocolEncoder extends MessageToByteEncoder<Command> implements Encoder {

    private static final Logger logger = LoggerFactory.getLogger(Encoder.class);

    @Override
    protected final void encode(ChannelHandlerContext ctx, Command command, ByteBuf out) throws Exception {
        try {
            byte[] bytes = encodeCommand(ctx, command);

            StringBuilder s = new StringBuilder();
            s.append(String.format("[%s] ", ctx.channel().id()));
            s.append("ID: ").append(encodeDeviceId(command.getDeviceId())).append(", ");
            s.append("CMD: ").append(command.getType()).append(" ");
            if (bytes != null) {
                s.append("SENT");
            } else {
                s.append("NOT SENT");
            }

            logger.info(s.toString());

            out.writeBytes(bytes);
        } catch (Exception e) {
            logger.error("Encoder.encode exception", e);
        }
    }

    private byte[] encodeCommand(ChannelHandlerContext ctx, Command command) throws Exception {
        return encodeCommand(command);
    }
}
