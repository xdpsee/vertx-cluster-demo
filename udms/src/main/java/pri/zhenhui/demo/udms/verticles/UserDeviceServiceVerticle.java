package pri.zhenhui.demo.udms.verticles;

import pri.zhenhui.demo.support.microservice.AbstractMicroServiceVerticle;
import pri.zhenhui.demo.udms.service.UserDeviceService;
import pri.zhenhui.demo.udms.service.impl.UserDeviceServiceImpl;

public class UserDeviceServiceVerticle extends AbstractMicroServiceVerticle<UserDeviceService> {

    public UserDeviceServiceVerticle() {
        super(UserDeviceService.SERVICE_NAME, UserDeviceService.SERVICE_ADDRESS, UserDeviceService.class);
    }

    @Override
    protected UserDeviceService serviceImpl() {
        return new UserDeviceServiceImpl(vertx.getOrCreateContext(), serviceDiscovery());
    }
}
