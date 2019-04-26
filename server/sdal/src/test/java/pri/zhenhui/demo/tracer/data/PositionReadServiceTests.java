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
import pri.zhenhui.demo.tracer.service.PositionReadService;
import pri.zhenhui.demo.tracer.service.PositionWriteService;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(VertxExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PositionReadServiceTests {

    private ServiceDiscovery serviceDiscovery;

    @BeforeAll
    public void setup(Vertx vertx, VertxTestContext context) {

        serviceDiscovery = ServiceDiscovery.create(vertx);

        DBUtils.clearDB(vertx);

        vertx.deployVerticle(new MainVerticle(), context.completing());
    }

    @Test
    public void testQueryLastPosition(VertxTestContext context) {

        ServiceReference wrReference = serviceDiscovery.getReference(EventBusService.createRecord(PositionWriteService.SERVICE_NAME, PositionWriteService.SERVICE_ADDRESS, PositionWriteService.class));
        ServiceReference rdReference = serviceDiscovery.getReference(EventBusService.createRecord(PositionReadService.SERVICE_NAME, PositionReadService.SERVICE_ADDRESS, PositionReadService.class));

        try {
            PositionWriteService positionWriteService = wrReference.getAs(PositionWriteService.class);
            PositionReadService positionReadService = rdReference.getAs(PositionReadService.class);

            Position position = new Position();
            position.setId(2L);
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
                if (save.failed()) {
                    context.failNow(save.cause());
                    return;
                }

                Long posId = save.result();
                positionReadService.queryLastPosition(UniqueID.valueOf("IMEI-888888888888888"), queryLastPos -> {
                    if (queryLastPos.failed()) {
                        context.failNow(queryLastPos.cause());
                    } else {
                        try {
                            assertTrue(queryLastPos.succeeded()
                                    && queryLastPos.result() != null
                                    && queryLastPos.result().getId().equals(posId));
                            context.completeNow();
                        } catch (Throwable e) {
                            context.failNow(e);
                        }
                    }
                });

            });

        } finally {
            wrReference.release();
            rdReference.release();
        }
    }

    @Test
    public void testLsLastPosition(VertxTestContext context) {

        ServiceReference wrReference = serviceDiscovery.getReference(EventBusService.createRecord(PositionWriteService.SERVICE_NAME, PositionWriteService.SERVICE_ADDRESS, PositionWriteService.class));
        ServiceReference rdReference = serviceDiscovery.getReference(EventBusService.createRecord(PositionReadService.SERVICE_NAME, PositionReadService.SERVICE_ADDRESS, PositionReadService.class));

        try {
            PositionWriteService positionWriteService = wrReference.getAs(PositionWriteService.class);
            PositionReadService positionReadService = rdReference.getAs(PositionReadService.class);

            Position position = new Position();
            position.setId(3L);
            position.setDeviceId(UniqueID.valueOf("IMEI-888888888888888"));
            position.setTime(new Date());
            position.setLocated(true);
            position.setLatitude(33.12300);
            position.setLongitude(120.07605);
            position.setAltitude(100.2);
            position.setCourse(23.6);
            position.setAccuracy(2.3);
            position.setSpeed(13.6);
            position.setNetwork(new Network());

            positionWriteService.savePosition(position, save -> {
                if (save.failed()) {
                    context.failNow(save.cause());
                    return;
                }

                Position currPos = position;
                positionReadService.isLastPosition(currPos, queryIsLastPos -> {
                    if (queryIsLastPos.failed()) {
                        context.failNow(queryIsLastPos.cause());
                    } else {
                        try {
                            assertTrue(queryIsLastPos.result());
                            context.completeNow();
                        } catch (Throwable e) {
                            context.failNow(e);
                        }
                    }
                });
            });

        } finally {
            wrReference.release();
            rdReference.release();
        }

    }
}
