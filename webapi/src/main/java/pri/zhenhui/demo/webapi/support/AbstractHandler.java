package pri.zhenhui.demo.webapi.support;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.reactivex.ext.web.RoutingContext;

public abstract class AbstractHandler implements Handler<RoutingContext> {

    protected AppContext appContext;

    protected AbstractHandler(AppContext appContext) {
        this.appContext = appContext;
    }

    protected void write(RoutingContext context, Result result) {
        context.response()
                .putHeader("Content-Type", "application/json;charset=utf-8")
                .end(Json.encode(result));
    }

    protected <T> void executeBlocking(Handler<io.vertx.reactivex.core.Future<T>> blockingCodeHandler, Handler<AsyncResult<T>> resultHandler) {
        appContext.vertx().executeBlocking(blockingCodeHandler, resultHandler);
    }
}

