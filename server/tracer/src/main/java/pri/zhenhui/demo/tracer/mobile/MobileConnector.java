package pri.zhenhui.demo.tracer.mobile;

import io.netty.channel.ChannelPipeline;
import pri.zhenhui.demo.tracer.mobile.codec.MobileFrameDecoder;
import pri.zhenhui.demo.tracer.mobile.codec.MobileProtocolDecoder;
import pri.zhenhui.demo.tracer.mobile.codec.MobileProtocolEncoder;
import pri.zhenhui.demo.tracer.server.Protocol;
import pri.zhenhui.demo.tracer.support.handler.DefaultDataHandler;
import pri.zhenhui.demo.tracer.support.handler.event.OverspeedEventHandler;
import pri.zhenhui.demo.tracer.support.server.AbstractConnector;
import pri.zhenhui.demo.tracer.support.server.ServerContext;

public class MobileConnector extends AbstractConnector {

    public MobileConnector(ServerContext context) {
        super(context);
    }

    @Override
    public Protocol protocol() {
        return new MobileProtocol();
    }

    @Override
    public void initPipeline(ChannelPipeline pipeline) {
        pipeline.addLast("frameDecoder", new MobileFrameDecoder())
                .addLast("protocolDecoder", new MobileProtocolDecoder())
                .addLast("dataHandler", new DefaultDataHandler(this))
                .addLast("eventHandler", new OverspeedEventHandler(this));

        pipeline.addLast("protocolEncoder", new MobileProtocolEncoder());
    }
}


