package pri.zhenhui.demo.webapi.handlers.security;

import io.vertx.ext.web.RoutingContext;
import pri.zhenhui.demo.webapi.support.AbstractHandler;
import pri.zhenhui.demo.webapi.support.AppContext;

public class LogoutHandler extends AbstractHandler {

    public LogoutHandler(AppContext appContext) {
        super(appContext);
    }

    @Override
    public void handle(RoutingContext context) {

    }
}
