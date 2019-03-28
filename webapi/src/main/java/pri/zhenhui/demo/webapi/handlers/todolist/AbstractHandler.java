package pri.zhenhui.demo.webapi.handlers.todolist;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.types.EventBusService;

public abstract class AbstractHandler implements Handler<RoutingContext> {

    private ServiceDiscovery serviceDiscovery;

    protected AbstractHandler(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    protected <T> T getService(String name, String address, Class<T> tClass) {

        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(name, address, tClass));
        try {
            return reference.getAs(tClass);
        } finally {
            reference.release();
        }

    }
}
