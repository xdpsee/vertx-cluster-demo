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
    private ContextInternal context;

    private final ServerConnector connector;

    private ServerBootstrap bootstrap;
    private Channel serverChannel;

    public DefaultTraceServer(Vertx vertx, ServerConnector connector) {
        this.vertx = (VertxInternal) vertx;
        this.connector = connector;
    }

    @Override
    public void listen(int port, String host, Handler<AsyncResult<Void>> listenHandler) {

        if (bootstrap != null) {
            throw new IllegalStateException("Already started");
        }

        context = vertx.getOrCreateContext();

        initBootstrap();

        bind(host, port, listenHandler);
    }

    private void initBootstrap() {
        EventLoopGroup acceptorGroup = vertx.getAcceptorEventLoopGroup();
        EventLoop eventLoop = context.nettyEventLoop();
        bootstrap = new ServerBootstrap();
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.group(acceptorGroup, eventLoop);
        bootstrap.childHandler(new PipelineInitializer<>(connector));
    }

    private void bind(String host, int port, Handler<AsyncResult<Void>> listenHandler) {
        ChannelFuture bindFuture = bootstrap.bind(host, port);
        bindFuture.addListener((ChannelFutureListener) future -> context.executeFromIO(v -> {
            if (future.isSuccess()) {
                serverChannel = future.channel();
                listenHandler.handle(Future.succeededFuture(null));
            } else {
                listenHandler.handle(Future.failedFuture(future.cause()));
            }
        }));
    }

    @Override
    public void close() {
        if (serverChannel != null) {
            serverChannel.close();
            serverChannel = null;
        }
    }

}

