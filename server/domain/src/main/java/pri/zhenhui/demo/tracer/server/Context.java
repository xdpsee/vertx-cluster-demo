package pri.zhenhui.demo.tracer.server;

import io.reactivex.Maybe;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.reactivex.core.Future;

public interface Context {

    <T> T getService(String name, String address, Class<T> serviceClass);

    <T> Maybe<T> rxExecuteBlocking(Handler<Future<T>> blockingCodeHandler);

    <T> void executeBlocking(Handler<Future<T>> blockingCodeHandler, Handler<AsyncResult<T>> resultHandler);

}
