package pri.zhenhui.demo.tracer.support.server;

import io.netty.channel.*;
import io.netty.channel.socket.DatagramChannel;
import pri.zhenhui.demo.tracer.server.Connection;
import pri.zhenhui.demo.tracer.server.ServerConnector;
import pri.zhenhui.demo.tracer.utils.ChannelAttribute;
import pri.zhenhui.demo.tracer.utils.ChannelAttributesUtils;

public final class PipelineInitializer<C extends Channel> extends ChannelInitializer<C> {

    private final ServerConnector connector;

    public PipelineInitializer(ServerConnector connector) {
        this.connector = connector;
    }

    @Override
    protected void initChannel(C channel) throws Exception {

        ChannelPipeline pipeline = channel.pipeline();

        pipeline.addFirst("channel-open", new OpenChannelHandler(connector));

        connector.initPipeline(pipeline);
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


