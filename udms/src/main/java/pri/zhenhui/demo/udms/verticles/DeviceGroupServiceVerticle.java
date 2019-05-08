package pri.zhenhui.demo.udms.verticles;

import pri.zhenhui.demo.support.microservice.AbstractMicroServiceVerticle;
import pri.zhenhui.demo.udms.service.DeviceGroupService;
import pri.zhenhui.demo.udms.service.impl.DeviceGroupServiceImpl;

public class DeviceGroupServiceVerticle extends AbstractMicroServiceVerticle<DeviceGroupService> {

    public DeviceGroupServiceVerticle() {
        super(DeviceGroupService.SERVICE_NAME, DeviceGroupService.SERVICE_ADDRESS, DeviceGroupService.class);
    }

    @Override
    protected DeviceGroupService serviceImpl() {
        return new DeviceGroupServiceImpl(vertx.getOrCreateContext());
    }
}

