package pri.zhenhui.demo.tracer.support.server;

import io.reactivex.Maybe;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.reactivex.core.Future;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.file.FileSystem;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.types.EventBusService;
import pri.zhenhui.demo.tracer.server.Context;

public final class ServerContext implements Context {

    private final Vertx vertx;

    private final ServiceDiscovery discovery;

    public ServerContext(Vertx vertx, ServiceDiscovery serviceDiscovery) {
        this.vertx = vertx;
        this.discovery = serviceDiscovery;
    }

    public <T> T getService(String name, String address, Class<T> serviceClass) {
        final ServiceReference reference = this.discovery.getReference(EventBusService.createRecord(name, address, serviceClass));
        return reference.getAs(serviceClass);
    }

    public <T> Maybe<T> rxExecuteBlocking(Handler<io.vertx.reactivex.core.Future<T>> blockingCodeHandler) {
        return vertx.rxExecuteBlocking(blockingCodeHandler);
    }

    public <T> void executeBlocking(Handler<Future<T>> blockingCodeHandler, Handler<AsyncResult<T>> resultHandler) {
        vertx.executeBlocking(blockingCodeHandler, resultHandler);
    }

    public FileSystem fileSystem() {
        return vertx.fileSystem();
    }
}


