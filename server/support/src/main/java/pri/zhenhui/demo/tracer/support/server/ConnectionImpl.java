package pri.zhenhui.demo.tracer.support.server;

import io.netty.channel.Channel;
import pri.zhenhui.demo.tracer.domain.Command;
import pri.zhenhui.demo.tracer.domain.UniqueID;
import pri.zhenhui.demo.tracer.server.Connection;
import pri.zhenhui.demo.tracer.server.Protocol;
import pri.zhenhui.demo.tracer.utils.ChannelAttribute;
import pri.zhenhui.demo.tracer.utils.ChannelAttributesUtils;

public class ConnectionImpl implements Connection {

    private final Channel channel;

    private final Protocol protocol;

    public ConnectionImpl(Channel channel, Protocol protocol) {
        this.channel = channel;
        this.protocol = protocol;
    }

    @Override
    public UniqueID deviceId() {
        return (UniqueID) ChannelAttributesUtils.get(channel, ChannelAttribute.DEVICE_ID);
    }

    @Override
    public Channel channel() {
        return channel;
    }
    
    @Override
    public Protocol protocol() {
        return protocol;
    }

    @Override
    public void send(Command command) {
        protocol.sendCommand(this, command);
    }

    @Override
    public void write(Object message) {
        channel.writeAndFlush(message);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        Connection that = (Connection)o;
        return channel.id().equals(that.channel().id());
    }

    @Override
    public int hashCode() {
        return this.channel.hashCode();
    }

}
