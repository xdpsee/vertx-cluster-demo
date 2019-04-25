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
import pri.zhenhui.demo.tracer.domain.Position;
import pri.zhenhui.demo.tracer.domain.UniqueID;
import pri.zhenhui.demo.tracer.domain.misc.Network;
import pri.zhenhui.demo.tracer.service.PositionWriteService;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(VertxExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PositionWriteServiceTests {

    private ServiceDiscovery serviceDiscovery;

    @BeforeAll
    public void setup(Vertx vertx, VertxTestContext context) {

        serviceDiscovery = ServiceDiscovery.create(vertx);

        DBUtils.clearDB(vertx);

        vertx.deployVerticle(new MainVerticle(), context.completing());
    }

    @Test
    public void testSavePosition(VertxTestContext context) {

        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(PositionWriteService.SERVICE_NAME, PositionWriteService.SERVICE_ADDRESS, PositionWriteService.class));

        try {
            PositionWriteService positionWriteService = reference.getAs(PositionWriteService.class);

            Position position = new Position();
            position.setDeviceId(UniqueID.valueOf("IMEI-888888888888888"));
            position.setTime(new Date());
            position.setLocated(true);
            position.setLatitude(33.12304);
            position.setLongitude(120.07645);
            position.setAltitude(100.2);
            position.setCourse(23.6);
            position.setAccuracy(2.3);
            position.setSpeed(13.6);
            position.setNetwork(new Network());

            positionWriteService.savePosition(position, save -> {
                try {
                    assertTrue(save.succeeded());
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
