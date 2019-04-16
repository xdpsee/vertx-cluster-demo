package pri.zhenhui.demo.tracer.data.service;

import pri.zhenhui.demo.support.AbstractMicroServiceVerticle;
import pri.zhenhui.demo.tracer.service.DeviceReadService;

public class DeviceReadServiceVerticle extends AbstractMicroServiceVerticle<DeviceReadService> {

    public DeviceReadServiceVerticle() {
        super(DeviceReadService.SERVICE_NAME, DeviceReadService.SERVICE_ADDRESS, DeviceReadService.class);
    }

    @Override
    protected DeviceReadService serviceImpl() {
        return new DeviceReadServiceImpl(vertx.getOrCreateContext());
    }
}


