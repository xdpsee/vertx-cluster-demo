package pri.zhenhui.demo.tracer.data.verticles;

import pri.zhenhui.demo.support.microservice.AbstractMicroServiceVerticle;
import pri.zhenhui.demo.tracer.data.service.DeviceReadServiceImpl;
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


