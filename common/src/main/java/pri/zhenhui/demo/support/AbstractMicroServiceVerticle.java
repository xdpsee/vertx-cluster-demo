package pri.zhenhui.demo.support;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.types.EventBusService;
import io.vertx.serviceproxy.ServiceBinder;

@SuppressWarnings("unused")
public abstract class AbstractMicroServiceVerticle<S> extends AbstractVerticle {

    private ServiceBinder serviceBinder;

    private MessageConsumer<JsonObject> registry;

    private ServiceDiscovery serviceDiscovery;

    private Record serviceRecord;

    private final String serviceAddress;

    private final Class<S> serviceClass;

    public AbstractMicroServiceVerticle(String serviceName, String serviceAddress, Class<S> serviceClass) {
        this.serviceAddress = serviceAddress;
        this.serviceClass = serviceClass;
        this.serviceRecord = EventBusService.createRecord(serviceName, serviceAddress, serviceClass);
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {

        start();

        final S service = this.serviceImpl();
        serviceBinder = new ServiceBinder(vertx);
        registry = serviceBinder.setAddress(serviceAddress).register(serviceClass, service);

        serviceDiscovery = ServiceDiscovery.create(vertx);
        serviceDiscovery.publish(serviceRecord, published -> {
            if (published.failed()) {
                startFuture.fail(published.cause());
            } else {
                startFuture.complete();
            }
        });
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {

        stop();

        serviceBinder.unregister(registry);
        serviceDiscovery.unpublish(serviceRecord.getRegistration(), unpublished -> {
            if (unpublished.failed()) {
                stopFuture.fail(unpublished.cause());
            } else {
                stopFuture.complete();
            }
        });
    }

    protected abstract S serviceImpl();
}
