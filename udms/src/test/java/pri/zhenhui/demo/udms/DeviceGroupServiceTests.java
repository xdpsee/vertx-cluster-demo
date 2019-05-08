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
import pri.zhenhui.demo.udms.service.DeviceGroupService;
import pri.zhenhui.demo.udms.utils.DBUtils;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(VertxExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeviceGroupServiceTests {

    private ServiceDiscovery serviceDiscovery;

    @BeforeAll
    public void setup(Vertx vertx, VertxTestContext context) {

        serviceDiscovery = ServiceDiscovery.create(vertx);

        DBUtils.clearDB(vertx);

        vertx.deployVerticle(new MainVerticle(), context.completing());
    }

    @Test
    public void testCreateGroup(VertxTestContext context) {

        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(DeviceGroupService.SERVICE_NAME, DeviceGroupService.SERVICE_ADDRESS, DeviceGroupService.class));
        try {
            DeviceGroupService deviceGroupService = reference.getAs(DeviceGroupService.class);

            deviceGroupService.createGroup(1L, "设备分组1", createGroup -> {
                if (createGroup.failed()) {
                    context.failNow(createGroup.cause());
                } else {
                    context.completeNow();
                }
            });
        } finally {
            reference.release();
        }

    }

    @Test
    public void testRemoveGroup(VertxTestContext context) {

        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(DeviceGroupService.SERVICE_NAME, DeviceGroupService.SERVICE_ADDRESS, DeviceGroupService.class));
        try {
            DeviceGroupService deviceGroupService = reference.getAs(DeviceGroupService.class);

            deviceGroupService.createGroup(2L, "设备分组2", createGroup -> {
                if (createGroup.failed()) {
                    context.failNow(createGroup.cause());
                } else {
                    deviceGroupService.removeGroup(2L, createGroup.result(), removeGroup -> {
                        if (removeGroup.failed()) {
                            context.failNow(removeGroup.cause());
                        } else {
                            context.completeNow();
                        }
                    });
                }
            });
        } finally {
            reference.release();
        }

    }

    @Test
    public void testAddDeviceToGroup(VertxTestContext context) {

        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(DeviceGroupService.SERVICE_NAME, DeviceGroupService.SERVICE_ADDRESS, DeviceGroupService.class));
        try {
            DeviceGroupService deviceGroupService = reference.getAs(DeviceGroupService.class);

            deviceGroupService.createGroup(3L, "设备分组3", createGroup -> {
                if (createGroup.failed()) {
                    context.failNow(createGroup.cause());
                } else {
                    deviceGroupService.addDeviceToGroup(createGroup.result(), UniqueID.valueOf("IMEI-1234567891234567"), addDeviceToGroup -> {
                        if (addDeviceToGroup.failed()) {
                            context.failNow(addDeviceToGroup.cause());
                        } else {
                            context.completeNow();
                        }
                    });
                }
            });
        } finally {
            reference.release();
        }


    }

    @Test
    public void testRemoveDeviceFromGroup(VertxTestContext context) {

        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(DeviceGroupService.SERVICE_NAME, DeviceGroupService.SERVICE_ADDRESS, DeviceGroupService.class));
        try {
            DeviceGroupService deviceGroupService = reference.getAs(DeviceGroupService.class);

            deviceGroupService.createGroup(4L, "设备分组4", createGroup -> {
                if (createGroup.failed()) {
                    context.failNow(createGroup.cause());
                } else {
                    deviceGroupService.addDeviceToGroup(createGroup.result(), UniqueID.valueOf("IMEI-1234567891234567"), addDeviceToGroup -> {
                        if (addDeviceToGroup.failed()) {
                            context.failNow(addDeviceToGroup.cause());
                            return;
                        }

                        deviceGroupService.removeDeviceFromGroup(createGroup.result(), UniqueID.valueOf("IMEI-1234567891234567"), removeDeviceFromGroup -> {
                            if (removeDeviceFromGroup.failed()) {
                                context.failNow(removeDeviceFromGroup.cause());
                            } else {
                                context.completeNow();
                            }
                        });
                    });
                }
            });
        } finally {
            reference.release();
        }
    }

    @Test
    public void testQueryGroups(VertxTestContext context) {

        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(DeviceGroupService.SERVICE_NAME, DeviceGroupService.SERVICE_ADDRESS, DeviceGroupService.class));
        try {
            DeviceGroupService deviceGroupService = reference.getAs(DeviceGroupService.class);

            deviceGroupService.createGroup(5L, "设备分组5", createGroup -> {
                if (createGroup.failed()) {
                    context.failNow(createGroup.cause());
                } else {
                    deviceGroupService.queryGroups(5L, queryGroups -> {
                        try {
                            assertTrue(queryGroups.succeeded()
                                    && queryGroups.result().stream().allMatch(e -> e.getId().equals(createGroup.result())));
                            context.completeNow();
                        } catch (Throwable e) {
                            context.failNow(e);
                        }
                    });
                }
            });
        } finally {
            reference.release();
        }

    }

    @Test
    public void testQueryGroupDevices(VertxTestContext context) {

        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(DeviceGroupService.SERVICE_NAME, DeviceGroupService.SERVICE_ADDRESS, DeviceGroupService.class));
        try {
            DeviceGroupService deviceGroupService = reference.getAs(DeviceGroupService.class);

            deviceGroupService.createGroup(6L, "设备分组6", createGroup -> {
                if (createGroup.failed()) {
                    context.failNow(createGroup.cause());
                } else {
                    deviceGroupService.addDeviceToGroup(createGroup.result(), UniqueID.valueOf("IMEI-1234567891234567"), addDeviceToGroup -> {
                        if (addDeviceToGroup.failed()) {
                            context.failNow(addDeviceToGroup.cause());
                            return;
                        }

                        deviceGroupService.queryGroupDevices(createGroup.result(), queryGroupDevices -> {
                            try {
                                assertTrue(queryGroupDevices.succeeded()
                                        && queryGroupDevices.result().stream().allMatch(e -> e.equals(UniqueID.valueOf("IMEI-1234567891234567"))));
                                context.completeNow();
                            } catch (Throwable e) {
                                context.failNow(e);
                            }
                        });
                    });
                }
            });
        } finally {
            reference.release();
        }
    }
}
