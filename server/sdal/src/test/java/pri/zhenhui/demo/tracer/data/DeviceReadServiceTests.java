package pri.zhenhui.demo.tracer.data;

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
import pri.zhenhui.demo.tracer.data.utils.DBUtils;
import pri.zhenhui.demo.tracer.domain.UniqueID;
import pri.zhenhui.demo.tracer.service.DeviceReadService;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(VertxExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeviceReadServiceTests {

    private ServiceDiscovery serviceDiscovery;

    @BeforeAll
    public void setup(Vertx vertx, VertxTestContext context) {

        serviceDiscovery = ServiceDiscovery.create(vertx);

        DBUtils.clearDB(vertx);

        vertx.deployVerticle(new MainVerticle(), context.completing());
    }

    @Test
    public void testQueryDevice(VertxTestContext context) {

        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(DeviceReadService.SERVICE_NAME, DeviceReadService.SERVICE_ADDRESS, DeviceReadService.class));

        try {

            DeviceReadService deviceReadService = reference.getAs(DeviceReadService.class);
            deviceReadService.queryDevice(UniqueID.valueOf("IMEI-888888888888888"), queryDevice -> {

                try {
                    assertTrue(queryDevice.succeeded());


                } finally {
                    context.completeNow();
                }

            });

        } finally {
            reference.release();
        }
    }
}
