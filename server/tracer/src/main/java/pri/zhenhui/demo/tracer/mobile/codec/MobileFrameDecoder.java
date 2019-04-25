package pri.zhenhui.demo.tracer.mobile.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

public class MobileFrameDecoder extends ByteToMessageDecoder {

    private static final byte[] MAGIC_CHUNK= new byte[] {'#', '#'};

    /**
     * ##1,IMEI#
     * ##2,IMEI,latitude,longitude,altitude,course,speed,time,outdated,alarms=[no|sos|low_battery]#
     * @param ctx ChannelHandlerContext
     * @param in ByteBuf
     * @param out messages
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        final int bytes = in.readableBytes();
        if (!in.isReadable() || bytes < 4) {
            return;
        }

        final int start = in.readerIndex();
        in.markReaderIndex();

        byte[] magic = new byte[2];
        in.getBytes(start, magic);// 读取2字节

        if (!Arrays.equals(magic, MAGIC_CHUNK)) {
            in.skipBytes(1);
            return;
        }

        final byte type = in.getByte(start + MAGIC_CHUNK.length);
        if (type != '1' && type != '2') {
            in.skipBytes(1);
            return;
        }

        int curr = start + MAGIC_CHUNK.length + 1;
        byte currByte;
        do {
            currByte = in.getByte(curr++);
        } while (in.isReadable() && currByte != '#');

        if (currByte == '#') {
            final byte[] body = new byte[curr - start];
            in.getBytes(start, body);
            in.skipBytes(body.length);

            out.add(new String(body, Charset.forName("UTF-8")));
        }
    }
}



