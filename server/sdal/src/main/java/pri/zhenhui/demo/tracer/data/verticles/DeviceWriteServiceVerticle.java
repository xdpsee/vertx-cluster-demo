package pri.zhenhui.demo.tracer.data.verticles;

import pri.zhenhui.demo.support.microservice.AbstractMicroServiceVerticle;
import pri.zhenhui.demo.tracer.data.service.impl.DeviceWriteServiceImpl;
import pri.zhenhui.demo.tracer.service.DeviceWriteService;

public class DeviceWriteServiceVerticle extends AbstractMicroServiceVerticle<DeviceWriteService> {

    public DeviceWriteServiceVerticle() {
        super(DeviceWriteService.SERVICE_NAME, DeviceWriteService.SERVICE_ADDRESS, DeviceWriteService.class);
    }

    @Override
    protected DeviceWriteService serviceImpl() {
        return new DeviceWriteServiceImpl(vertx.getOrCreateContext());
    }
}