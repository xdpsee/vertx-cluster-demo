package pri.zhenhui.demo.webapi.support;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

public abstract class AbstractHandler implements Handler<RoutingContext> {

    protected AppContext appContext;

    protected AbstractHandler(AppContext appContext) {
        this.appContext = appContext;
    }

}

