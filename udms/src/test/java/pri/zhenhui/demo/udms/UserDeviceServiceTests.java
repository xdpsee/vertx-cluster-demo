package pri.zhenhui.demo.udms;

import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.types.EventBusService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import pri.zhenhui.demo.tracer.domain.UniqueID;
import pri.zhenhui.demo.udms.service.UserDeviceService;
import pri.zhenhui.demo.udms.utils.DBUtils;

@ExtendWith(VertxExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDeviceServiceTests {

    private ServiceDiscovery serviceDiscovery;

    @BeforeAll
    public void setup(Vertx vertx, VertxTestContext context) {

        serviceDiscovery = ServiceDiscovery.create(vertx);

        DBUtils.clearDB(vertx);

        vertx.deployVerticle(new MainVerticle(), context.completing());
    }

    @Test
    public void testAssignUserDevice(VertxTestContext context) {

        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(UserDeviceService.SERVICE_NAME, UserDeviceService.SERVICE_ADDRESS, UserDeviceService.class));
        try {
            UserDeviceService userDeviceService = reference.getAs(UserDeviceService.class);

            userDeviceService.assignDevice(1L, UniqueID.valueOf("IMEI-000000000000000"), createGroup -> {
                if (createGroup.failed()) {
                    context.failNow(createGroup.cause());
                    return;
                }

                context.completeNow();
            });

        } finally {
            reference.release();
        }
    }

    @Test
    public void testDeassignUserDevice(VertxTestContext context) {

        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(UserDeviceService.SERVICE_NAME, UserDeviceService.SERVICE_ADDRESS, UserDeviceService.class));
        try {
            UserDeviceService userDeviceService = reference.getAs(UserDeviceService.class);

            userDeviceService.assignDevice(1L, UniqueID.valueOf("IMEI-000000000000000"), createGroup -> {
                if (createGroup.failed()) {
                    context.failNow(createGroup.cause());
                    return;
                }

                userDeviceService.deassignDevice(1L, UniqueID.valueOf("IMEI-000000000000000"), deassignDevice -> {
                    if (deassignDevice.failed()) {
                        context.failNow(deassignDevice.cause());
                        return;
                    }
                    context.completeNow();
                });
            });

        } finally {
            reference.release();
        }
    }
}
