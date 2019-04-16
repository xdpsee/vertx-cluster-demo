package pri.zhenhui.demo.tracer.support.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.impl.ContextInternal;
import io.vertx.core.impl.VertxInternal;
import pri.zhenhui.demo.tracer.server.ServerConnector;
import pri.zhenhui.demo.tracer.server.TraceServer;

public final class DefaultTraceServer implements TraceServer {

    private final VertxInternal vertx;

    private final ContextInternal context;

    private final ServerConnector connector;

    private ServerBootstrap bootstrap;

    private Channel channel;

    public DefaultTraceServer(Vertx vertx, ServerConnector connector) {
        this.vertx = (VertxInternal) vertx;
        this.context = (ContextInternal) vertx.getOrCreateContext();
        this.connector = connector;
    }

    @Override
    public void listen(int port, String host, Handler<AsyncResult<Void>> listenHandler) {

        if (bootstrap != null) {
            throw new IllegalStateException("Already started");
        }

        EventLoopGroup acceptorGroup = vertx.getAcceptorEventLoopGroup();
        EventLoop eventLoop = context.nettyEventLoop();
        bootstrap = new ServerBootstrap();
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.group(acceptorGroup, eventLoop);
        bootstrap.childHandler(new PipelineInitializer<>(connector));

        ChannelFuture bindFuture = bootstrap.bind(host, port);
        bindFuture.addListener((ChannelFutureListener) future -> context.executeFromIO(v -> {
            if (future.isSuccess()) {
                channel = future.channel();
                listenHandler.handle(Future.succeededFuture(null));
            } else {
                listenHandler.handle(Future.failedFuture(future.cause()));
            }
        }));
    }

    @Override
    public void close() {
        if (channel != null) {
            channel.close();
            channel = null;
        }
    }

}

