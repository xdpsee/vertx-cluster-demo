package pri.zhenhui.demo.support.microservice;

import io.reactivex.Completable;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.types.EventBusService;
import io.vertx.serviceproxy.ServiceBinder;

@SuppressWarnings("unused")
public abstract class AbstractMicroServiceVerticle<S> extends AbstractVerticle {

    private ServiceBinder serviceBinder;

    private MessageConsumer<JsonObject> registerConsumer;

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
                registerConsumer = serviceBinder.setAddress(serviceAddress).register(serviceClass, service);

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

                serviceBinder.unregister(registerConsumer);
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

    protected final ServiceDiscovery serviceDiscovery() {
        return serviceDiscovery;
    }

    protected final ServiceReference getServiceReference(String name, String address, Class<S> serviceClass) {

        return serviceDiscovery.getReference(EventBusService.createRecord(name, address, serviceClass));

    }
}
