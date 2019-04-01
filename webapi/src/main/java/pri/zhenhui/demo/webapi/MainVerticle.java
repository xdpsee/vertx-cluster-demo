package pri.zhenhui.demo.webapi;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import pri.zhenhui.demo.webapi.handlers.security.LoginHandler;
import pri.zhenhui.demo.webapi.handlers.security.LogoutHandler;
import pri.zhenhui.demo.webapi.handlers.todolist.TodolistCreateHandler;
import pri.zhenhui.demo.webapi.support.AppContext;
import pri.zhenhui.demo.webapi.handlers.security.AuthenticationHandler;

public class MainVerticle extends AbstractVerticle {

    private AppContext appContext;

    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);

        this.appContext = AppContext.create(vertx);
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

        this.appContext.close();

        stopFuture.complete();
    }

    private Router createRouter() {

        Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create(false));

        router.post("/api/auth/login").handler(new LoginHandler(appContext));
        router.post("/api/auth/logout").handler(new LogoutHandler(appContext));

        router.route("/api/protected/*").handler(new AuthenticationHandler(appContext));

        router.get("/api/protected/todolist").handler(new TodolistCreateHandler(appContext));
        router.post("/api/protected/todolist").handler(new TodolistCreateHandler(appContext));
        router.put("/api/protected/todolist").handler(new TodolistCreateHandler(appContext));
        router.delete("/api/protected/todolist").handler(new TodolistCreateHandler(appContext));

        return router;

    }
}


