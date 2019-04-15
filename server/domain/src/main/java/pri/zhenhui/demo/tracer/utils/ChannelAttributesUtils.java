package pri.zhenhui.demo.tracer.utils;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

public class ChannelAttributesUtils {

    public static void setIfAbsent(Channel channel, ChannelAttribute attr, Object value) {
        if (null == channel || null == attr || null == value) {
            throw new IllegalArgumentException("");
        }

        channel.attr(AttributeKey.valueOf(attr.name())).setIfAbsent(value);
    }

    public static void set(Channel channel, ChannelAttribute attr, Object value) {
        if (null == channel || null == attr || null == value) {
            throw new IllegalArgumentException("");
        }

        channel.attr(AttributeKey.valueOf(attr.name())).set(value);
    }

    public static Object get(Channel channel, ChannelAttribute attr) {
        if (null == channel || null == attr) {
            throw new IllegalArgumentException("");
        }

        return channel.attr(AttributeKey.valueOf(attr.name())).get();
    }

    public static void setIfAbsent(ChannelHandlerContext ctx, ChannelAttribute attr, Object value) {
        setIfAbsent(ctx.channel(), attr, value);
    }

    public static void set(ChannelHandlerContext ctx, ChannelAttribute attr, Object value) {
        set(ctx.channel(), attr, value);
    }

    public static Object get(ChannelHandlerContext ctx, ChannelAttribute attr) {
        return get(ctx.channel(), attr);
    }


}
