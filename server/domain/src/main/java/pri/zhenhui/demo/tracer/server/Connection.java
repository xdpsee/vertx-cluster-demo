package pri.zhenhui.demo.tracer.server;

import io.netty.channel.Channel;
import pri.zhenhui.demo.tracer.domain.Command;
import pri.zhenhui.demo.tracer.domain.UniqueID;
import pri.zhenhui.demo.tracer.utils.ChannelAttribute;
import pri.zhenhui.demo.tracer.utils.ChannelAttributesUtils;

public class Connection {

    private final Channel channel;

    private final Protocol protocol;

    public Connection(Channel channel, Protocol protocol) {
        this.channel = channel;
        this.protocol = protocol;
    }

    public Channel getChannel() {
        return channel;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public UniqueID deviceId() {
        return (UniqueID) ChannelAttributesUtils.get(channel, ChannelAttribute.DEVICE_ID);
    }

    public void sendCommand(Command command) {
        protocol.sendCommand(this, command);
    }

    public void write(Object message) {
        channel.writeAndFlush(message);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        Connection that = (Connection)o;
        return channel.id().equals(that.channel.id());
    }

    @Override
    public int hashCode() {
        return this.channel.hashCode();
    }
}


