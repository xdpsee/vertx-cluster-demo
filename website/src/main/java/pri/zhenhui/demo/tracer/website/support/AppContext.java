package pri.zhenhui.demo.tracer.website.support;

import io.reactivex.Scheduler;
import io.vertx.reactivex.core.RxHelper;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.common.template.TemplateEngine;
import io.vertx.reactivex.ext.web.sstore.ClusteredSessionStore;
import io.vertx.reactivex.ext.web.sstore.SessionStore;
import io.vertx.reactivex.ext.web.templ.thymeleaf.ThymeleafTemplateEngine;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.types.EventBusService;

/**
 * @author zhenhui
 */
public class AppContext {

    private final Vertx vertx;

    private SessionStore sessionStore;

    private final ServiceDiscovery serviceDiscovery;

    private final ThymeleafTemplateEngine templateEngine;

    private final Scheduler scheduler;

    public static AppContext create(Vertx vertx) {
        return new AppContext(vertx, ServiceDiscovery.create(vertx.getDelegate()));
    }

    public void close() {
        this.serviceDiscovery.close();
    }

    public Vertx vertx() {return vertx;}

    public SessionStore sessionStore() {
        return this.sessionStore;
    }

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
        this.sessionStore = ClusteredSessionStore.create(vertx);
        this.serviceDiscovery = serviceDiscovery;
        this.templateEngine = ThymeleafTemplateEngine.create(vertx);
        this.scheduler = RxHelper.scheduler(vertx);
    }

    protected TemplateEngine templateEngine() {
        return this.templateEngine;
    }

    protected Scheduler scheduler() {
        return this.scheduler;
    }
}
