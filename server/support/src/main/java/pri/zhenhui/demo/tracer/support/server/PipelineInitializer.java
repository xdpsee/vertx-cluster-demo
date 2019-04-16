package pri.zhenhui.demo.tracer.support.server;

import io.netty.channel.*;
import io.netty.channel.socket.DatagramChannel;
import pri.zhenhui.demo.tracer.server.Connection;
import pri.zhenhui.demo.tracer.server.Encoder;
import pri.zhenhui.demo.tracer.server.Processor;
import pri.zhenhui.demo.tracer.server.ServerConnector;
import pri.zhenhui.demo.tracer.utils.ChannelAttribute;
import pri.zhenhui.demo.tracer.utils.ChannelAttributesUtils;

import java.util.List;

public final class PipelineInitializer<C extends Channel> extends ChannelInitializer<C> {

    private final ServerConnector connector;

    public PipelineInitializer(ServerConnector connector) {
        this.connector = connector;
    }

    @Override
    protected void initChannel(C channel) throws Exception {

        ChannelPipeline pipeline = channel.pipeline();

        pipeline.addFirst("channel-open", new OpenChannelHandler(connector));

        List<Processor> processors = connector.processors();
        for (Processor processor : processors) {
            pipeline.addLast(processor.getClass().getSimpleName(), processor);
        }

        Encoder encoder = connector.encoder();
        if (encoder != null) {
            pipeline.addLast(encoder.getClass().getSimpleName(), encoder);
        }
    }

    private class OpenChannelHandler extends ChannelInboundHandlerAdapter {
        private final ServerConnector connector;

        OpenChannelHandler(ServerConnector connector) {
            this.connector = connector;
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            if (!(ctx.channel() instanceof DatagramChannel)) {
                final Connection connection = new ConnectionImpl(ctx.channel(), connector.protocol());
                ChannelAttributesUtils.set(ctx, ChannelAttribute.CONNECTION, connection);
            }
        }
    }
}


