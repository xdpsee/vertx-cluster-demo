package pri.zhenhui.demo.tracer.website;


import io.reactivex.Completable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.reactivex.RxHelper;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.handler.BodyHandler;
import io.vertx.reactivex.ext.web.handler.StaticHandler;
import pri.zhenhui.demo.tracer.website.handlers.IndexHandler;
import pri.zhenhui.demo.tracer.website.support.AppContext;

/**
 * @author zhenhui
 */
public class MainVerticle extends AbstractVerticle {

    private AppContext appContext;

    private Scheduler scheduler;

    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);
        scheduler = RxHelper.scheduler(vertx);
    }

    @Override
    public Completable rxStart() {
        return Single.<Router>create(emitter -> {
            try {
                appContext = AppContext.create(vertx);
                emitter.onSuccess(createRouter());
            } catch (Throwable e) {
                emitter.onError(e);
            }
        }).flatMap(router -> vertx.createHttpServer()
                .requestHandler(router)
                .rxListen(8888)
        ).subscribeOn(scheduler).ignoreElement();
    }

    @Override
    public Completable rxStop() {
        return Single.create(emitter -> {
            try {
                appContext.close();
                emitter.onSuccess(true);
            } catch (Throwable e) {
                emitter.onError(e);
            }
        }).subscribeOn(scheduler).ignoreElement();
    }

    private Router createRouter() {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create(false));

        router.route("/static/*").handler(StaticHandler.create("webroot/static/").setCachingEnabled(false));

        router.route("/").handler(new IndexHandler(appContext));

        return router;
    }
}


