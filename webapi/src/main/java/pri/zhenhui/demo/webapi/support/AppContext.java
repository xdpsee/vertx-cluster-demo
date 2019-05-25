package pri.zhenhui.demo.webapi.support;

import io.vertx.reactivex.core.Vertx;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.types.EventBusService;

public final class AppContext {

    private final Vertx vertx;

    private final ServiceDiscovery serviceDiscovery;

    public static AppContext create(Vertx vertx) {

        return new AppContext(vertx, ServiceDiscovery.create(vertx.getDelegate()));

    }

    public void close() {
        this.serviceDiscovery.close();
    }

    public Vertx vertx() {return vertx;}


    public <T> T getService(String name, String address, Class<T> serviceClass) {

        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(name, address, serviceClass));
        try {
            return reference.getAs(serviceClass);
        } finally {
            reference.release();
        }
    }

    private AppContext(Vertx vertx, ServiceDiscovery serviceDiscovery) {
        this.vertx = vertx;
        this.serviceDiscovery = serviceDiscovery;
    }
}
