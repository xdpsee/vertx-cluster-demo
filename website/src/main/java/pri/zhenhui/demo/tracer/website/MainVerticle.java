package pri.zhenhui.demo.tracer.website;


import io.reactivex.Completable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.RxHelper;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.auth.AuthProvider;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.handler.*;
import io.vertx.reactivex.ext.web.sstore.ClusteredSessionStore;
import pri.zhenhui.demo.support.utils.ExceptionUtils;
import pri.zhenhui.demo.tracer.website.auth.AuthProviderImpl;
import pri.zhenhui.demo.tracer.website.support.LoginHandler;
import pri.zhenhui.demo.tracer.website.handlers.LogoutHandler;
import pri.zhenhui.demo.tracer.website.pages.IndexPage;
import pri.zhenhui.demo.tracer.website.pages.LoginPage;
import pri.zhenhui.demo.tracer.website.support.AppContext;
import pri.zhenhui.demo.tracer.website.support.AuthPreCheckHandler;

/**
 * @author zhenhui
 */
public class MainVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(MainVerticle.class);

    private AppContext appContext;

    private Scheduler scheduler;

    private AuthProvider authProvider;

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
                authProvider = new AuthProvider(new AuthProviderImpl(appContext));
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
        router.route().handler(CookieHandler.create());
        router.route().handler(SessionHandler.create(ClusteredSessionStore.create(vertx)));
        router.route().handler(UserSessionHandler.create(authProvider));
        router.route("/static/*").handler(StaticHandler.create("webroot/static/").setCachingEnabled(false));

        router.get("/login").handler(new LoginPage(appContext));
        router.post("/login").handler(LoginHandler.create(authProvider, appContext));
        router.get("/logout").handler(new LogoutHandler(appContext));

        router.get("/").handler(AuthPreCheckHandler.create(new IndexPage(appContext)));

        router.route().failureHandler(context -> {
            String exception = ExceptionUtils.getStackTrace(context.failure());
            context.response().setStatusCode(500).end(exception);
        });

        return router;
    }
}

