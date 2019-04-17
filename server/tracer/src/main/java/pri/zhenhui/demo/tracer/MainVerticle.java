package pri.zhenhui.demo.tracer;

import io.reactivex.Completable;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.servicediscovery.ServiceDiscovery;
import pri.zhenhui.demo.tracer.mobile.MobileVerticle;
import pri.zhenhui.demo.tracer.support.server.ServerContext;

@SuppressWarnings("unused")
public class MainVerticle extends AbstractVerticle {

    private ServerContext context;

    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);

        this.context = new ServerContext(this.vertx, ServiceDiscovery.create(vertx));

    }

    @Override
    public Completable rxStart() {

        return vertx.rxDeployVerticle(new MobileVerticle(context))
                .ignoreElement();

    }

}

