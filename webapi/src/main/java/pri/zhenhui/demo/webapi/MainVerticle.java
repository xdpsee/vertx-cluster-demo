package pri.zhenhui.demo.webapi;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.servicediscovery.ServiceDiscovery;
import pri.zhenhui.demo.webapi.handlers.todolist.TodolistCreateHandler;

public class MainVerticle extends AbstractVerticle {

    private ServiceDiscovery serviceDiscovery;

    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);

        this.serviceDiscovery = ServiceDiscovery.create(vertx);
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {

        vertx.createHttpServer().requestHandler(createRouter()).listen(8080, listen -> {
           if (listen.succeeded()) {
               startFuture.complete();
           } else {
               startFuture.fail(listen.cause());
           }
        });
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {

        this.serviceDiscovery.close();

        stopFuture.complete();
    }

    private Router createRouter() {

        Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create(false));
        router.post("/todolist").handler(new TodolistCreateHandler(serviceDiscovery));

        return router;

    }
}


