package pri.zhenhui.demo.tracer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import pri.zhenhui.demo.tracer.server.TraceServer;

public class MainVerticle extends AbstractVerticle {

    private TraceServer traceServer;

    @Override
    public void start(Future<Void> startFuture) throws Exception {

        startFuture.complete();
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {


        stopFuture.complete();
    }
}

