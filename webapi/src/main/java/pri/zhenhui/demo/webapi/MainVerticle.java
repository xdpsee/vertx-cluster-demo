package pri.zhenhui.demo.webapi;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.RxHelper;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.handler.BodyHandler;
import pri.zhenhui.demo.webapi.handlers.security.AuthenticationHandler;
import pri.zhenhui.demo.webapi.handlers.security.LoginHandler;
import pri.zhenhui.demo.webapi.handlers.security.LogoutHandler;
import pri.zhenhui.demo.webapi.handlers.todolist.TodolistCreateHandler;
import pri.zhenhui.demo.webapi.handlers.todolist.TodolistDeleteHandler;
import pri.zhenhui.demo.webapi.handlers.todolist.TodolistQueryHandler;
import pri.zhenhui.demo.webapi.handlers.todolist.TodolistUpdateHandler;
import pri.zhenhui.demo.webapi.support.AppContext;

public class MainVerticle extends AbstractVerticle {

    private AppContext appContext;

    @Override
    public Completable rxStart() {

        return Single.<Router>create(emitter -> {
            try {
                appContext = AppContext.create(vertx);
                Router router = createRouter();
                emitter.onSuccess(router);
            } catch (Exception e) {
                emitter.onError(e);
            }
        }).flatMap(router -> vertx.createHttpServer()
                .requestHandler(router)
                .rxListen(8080)
        ).subscribeOn(RxHelper.scheduler(vertx))
                .ignoreElement();

    }

    @Override
    public Completable rxStop() {
        return Single.create(emitter -> {
            try {
                appContext.close();
                emitter.onSuccess("appContext closed");
            } catch (Exception e) {
                emitter.onError(e);
            }
        }).ignoreElement();
    }

    private Router createRouter() {

        Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create(false));

        AuthenticationHandler authenticationHandler = new AuthenticationHandler(appContext);
        router.route("/api/protected/*").handler(authenticationHandler);
        router.route("/api/auth/logout").handler(authenticationHandler);

        router.post("/api/auth/login").handler(new LoginHandler(appContext));
        router.post("/api/auth/logout").handler(new LogoutHandler(appContext));


        router.get("/api/protected/todolist").handler(new TodolistQueryHandler(appContext));
        router.post("/api/protected/todolist").handler(new TodolistCreateHandler(appContext));
        router.put("/api/protected/todolist/:id").handler(new TodolistUpdateHandler(appContext));
        router.delete("/api/protected/todolist/:id").handler(new TodolistDeleteHandler(appContext));

        return router;

    }
}


