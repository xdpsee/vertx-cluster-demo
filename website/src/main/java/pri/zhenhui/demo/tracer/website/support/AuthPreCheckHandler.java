package pri.zhenhui.demo.tracer.website.support;


import io.vertx.core.Handler;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.ext.web.Session;

public class AuthPreCheckHandler implements Handler<RoutingContext> {

    private final Handler<RoutingContext> nextHandler;

    public static AuthPreCheckHandler create(Handler<RoutingContext> handler) {
        return new AuthPreCheckHandler(handler);
    }

    @Override
    public void handle(RoutingContext context) {
        if (null == context.user()) {
            Session session = context.session();
            session.put(Constants.RETURN_URL, context.request().path());

            context.response()
                    .setStatusCode(302)
                    .putHeader("location", "/login")
                    .end();
        } else {
            this.nextHandler.handle(context);
        }
    }

    private AuthPreCheckHandler(Handler<RoutingContext> handler) {
        this.nextHandler = handler;
    }
}
