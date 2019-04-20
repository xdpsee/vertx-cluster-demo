package pri.zhenhui.demo.tracer.mobile;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.vertx.reactivex.core.AbstractVerticle;
import pri.zhenhui.demo.tracer.server.ServerConnector;
import pri.zhenhui.demo.tracer.server.TraceServer;
import pri.zhenhui.demo.tracer.support.server.DefaultTraceServer;
import pri.zhenhui.demo.tracer.support.server.ServerContext;

public class MobileVerticle extends AbstractVerticle {

    private final ServerConnector connector;

    private TraceServer server;

    public MobileVerticle(ServerContext context) {
        this.connector = new MobileConnector(context);
    }

    @Override
    public Completable rxStart() {

        return Single.create(emitter -> {
            try {
                server = new DefaultTraceServer(vertx.getDelegate(), connector);
                server.listen(connector.configs().getInteger("port", 9527), "0.0.0.0", listen -> {
                    if (listen.failed()) {
                        emitter.onError(listen.cause());
                    } else {
                        emitter.onSuccess("OK");
                    }
                });
            } catch (Throwable e) {
                emitter.onError(e);
            }
        }).ignoreElement();

    }

    @Override
    public Completable rxStop() {

        if (server != null) {
            server.close();
        }

        return super.rxStop();

    }

}


