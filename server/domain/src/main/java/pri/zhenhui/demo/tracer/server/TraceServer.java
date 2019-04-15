package pri.zhenhui.demo.tracer.server;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

public interface TraceServer {

    void listen(int port, String host, Handler<AsyncResult<Void>> listenHandler);

    void close();

}


