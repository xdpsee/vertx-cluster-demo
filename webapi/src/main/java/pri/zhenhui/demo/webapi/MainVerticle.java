package pri.zhenhui.demo.webapi;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.handler.BodyHandler;
import pri.zhenhui.demo.webapi.handlers.security.AuthenticationHandler;
import pri.zhenhui.demo.webapi.handlers.security.LoginHandler;
import pri.zhenhui.demo.webapi.handlers.security.LogoutHandler;
import pri.zhenhui.demo.webapi.handlers.todolist.TodolistCreateHandler;
import pri.zhenhui.demo.webapi.handlers.todolist.TodolistQueryHandler;
import pri.zhenhui.demo.webapi.support.AppContext;

public class MainVerticle extends AbstractVerticle {

    private AppContext appContext;

    @Override
    public Completable rxStart() {

        return Single.create(emitter -> {
            appContext = AppContext.create(vertx);
            emitter.onSuccess("appContext created");
        }).ignoreElement()
                .andThen(vertx.createHttpServer().requestHandler(createRouter()).rxListen(8080))
                .ignoreElement();

    }

    @Override
    public Completable rxStop() {
        return Single.create(emitter -> {
            appContext.close();
            emitter.onSuccess("appContext closed");
        }).ignoreElement();
    }

    private Router createRouter() {

        Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create(false));

        router.post("/api/auth/login").handler(new LoginHandler(appContext));
        router.post("/api/auth/logout").handler(new LogoutHandler(appContext));

        router.route("/api/protected/*").handler(new AuthenticationHandler(appContext));

        router.get("/api/protected/todolist").handler(new TodolistQueryHandler(appContext));
        router.post("/api/protected/todolist").handler(new TodolistCreateHandler(appContext));
        router.put("/api/protected/todolist").handler(new TodolistCreateHandler(appContext));
        router.delete("/api/protected/todolist").handler(new TodolistCreateHandler(appContext));

        return router;

    }
}


