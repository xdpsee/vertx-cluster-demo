package pri.zhenhui.demo.tracer.server;

import io.netty.channel.SimpleChannelInboundHandler;

public abstract class Processor<T> extends SimpleChannelInboundHandler<T> {

}

