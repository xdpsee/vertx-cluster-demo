package pri.zhenhui.demo.tracer.website.handlers;

import io.vertx.reactivex.ext.web.RoutingContext;
import pri.zhenhui.demo.tracer.website.support.AbstractHandler;
import pri.zhenhui.demo.tracer.website.support.AppContext;

public class LogoutHandler extends AbstractHandler {

    public LogoutHandler(AppContext appContext) {
        super(appContext);
    }

    @Override
    public void handle(RoutingContext context) {
        context.clearUser();
        context.response()
                .setStatusCode(302)
                .putHeader("location", "/login")
                .end();
    }
}
