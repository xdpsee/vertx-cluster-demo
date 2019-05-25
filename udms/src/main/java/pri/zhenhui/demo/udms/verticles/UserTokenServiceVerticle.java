package pri.zhenhui.demo.udms.verticles;

import pri.zhenhui.demo.support.microservice.AbstractMicroServiceVerticle;
import pri.zhenhui.demo.udms.service.UserTokenService;
import pri.zhenhui.demo.udms.service.impl.UserTokenServiceImpl;

public class UserTokenServiceVerticle extends AbstractMicroServiceVerticle<UserTokenService> {

    public UserTokenServiceVerticle() {
        super(UserTokenService.SERVICE_NAME, UserTokenService.SERVICE_ADDRESS, UserTokenService.class);
    }

    @Override
    protected UserTokenService serviceImpl() {
        return new UserTokenServiceImpl(vertx);
    }
}
