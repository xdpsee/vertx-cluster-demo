package pri.zhenhui.demo.webapi.support;

import io.vertx.core.Vertx;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.types.EventBusService;

public final class AppContext {

    private final Vertx vertx;

    private final ServiceDiscovery serviceDiscovery;

    private final JWTAuth jwtAuth;


    public static AppContext create(Vertx vertx) {

        return new AppContext(vertx
                , ServiceDiscovery.create(vertx)
                , JWTAuth.create(vertx, new JWTAuthOptions()
                .addPubSecKey(new PubSecKeyOptions()
                        .setAlgorithm("HS256")
                        .setPublicKey("XmaDFytPLM4C@6MJ")
                        .setSymmetric(true))
        ));

    }

    public void close() {

        this.serviceDiscovery.close();

    }

    public Vertx vertx() {
        return vertx;
    }

    public JWTAuth jwtAuth() {
        return this.jwtAuth;
    }

    public <T> T getService(String name, String address, Class<T> serviceClass) {

        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(name, address, serviceClass));
        try {
            return reference.getAs(serviceClass);
        } finally {
            reference.release();
        }
    }

    private AppContext(Vertx vertx, ServiceDiscovery serviceDiscovery, JWTAuth jwtAuth) {
        this.vertx = vertx;
        this.serviceDiscovery = serviceDiscovery;
        this.jwtAuth = jwtAuth;
    }
}
