package pri.zhenhui.demo.support;

import io.reactivex.Completable;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
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
    public Completable rxStart() {

        return vertx.rxExecuteBlocking(future -> {
            try {
                start();

                final S service = this.serviceImpl();
                serviceBinder = new ServiceBinder(getVertx());
                registry = serviceBinder.setAddress(serviceAddress).register(serviceClass, service);

                serviceDiscovery = ServiceDiscovery.create(getVertx());
                serviceDiscovery.publish(serviceRecord, published -> {
                    if (published.failed()) {
                        future.fail(published.cause());
                    } else {
                        future.complete();
                    }
                });

            } catch (Throwable e) {
                future.fail(e.getCause());
            }
        }).ignoreElement();
    }

    @Override
    public Completable rxStop() {

        return vertx.rxExecuteBlocking(future -> {
            try {
                stop();

                serviceBinder.unregister(registry);
                serviceDiscovery.unpublish(serviceRecord.getRegistration(), unpublished -> {
                    if (unpublished.failed()) {
                        future.fail(unpublished.cause());
                    } else {
                        future.complete();
                    }
                });
            } catch (Throwable e) {
                future.fail(e);
            }
        }).ignoreElement();
    }

    protected abstract S serviceImpl();
}
