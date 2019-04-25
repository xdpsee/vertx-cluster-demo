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
import pri.zhenhui.demo.support.test.StackTrace;
import pri.zhenhui.demo.tracer.data.utils.DBUtils;
import pri.zhenhui.demo.tracer.domain.Device;
import pri.zhenhui.demo.tracer.domain.UniqueID;
import pri.zhenhui.demo.tracer.enums.DeviceStatus;
import pri.zhenhui.demo.tracer.service.DeviceWriteService;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(VertxExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeviceWriteServiceTests {

    private ServiceDiscovery serviceDiscovery;

    @BeforeAll
    public void setup(Vertx vertx, VertxTestContext context) {

        serviceDiscovery = ServiceDiscovery.create(vertx);

        DBUtils.clearDB(vertx);

        vertx.deployVerticle(new MainVerticle(), context.completing());
    }

    @Test
    public void testCreateDevice(VertxTestContext context) {

        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(DeviceWriteService.SERVICE_NAME, DeviceWriteService.SERVICE_ADDRESS, DeviceWriteService.class));

        try {
            DeviceWriteService deviceWriteService = reference.getAs(DeviceWriteService.class);

            Device device = new Device();
            device.setId(UniqueID.valueOf("IMEI-888888888888889"));
            device.setModel("mobile-test");
            device.setProtocol("mobile");
            device.setStatus(DeviceStatus.NORMAL);
            device.setCreateAt(new Date());
            device.setUpdateAt(new Date());

            deviceWriteService.createDevice(device, createDevice -> {
                try {
                    StackTrace.printIfErr(createDevice);
                    assertTrue(createDevice.succeeded() && createDevice.result());
                    context.completeNow();
                } catch (Throwable e) {
                    context.failNow(e);
                }
            });

        } finally {
            reference.release();
        }

    }
}


