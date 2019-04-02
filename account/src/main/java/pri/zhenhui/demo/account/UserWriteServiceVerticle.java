package pri.zhenhui.demo.account;

import pri.zhenhui.demo.support.AbstractMicroServiceVerticle;

public class UserWriteServiceVerticle extends AbstractMicroServiceVerticle<UserWriteService> {

    public UserWriteServiceVerticle() {
        super(UserWriteService.SERVICE_NAME, UserWriteService.SERVICE_ADDRESS, UserWriteService.class);
    }

    @Override
    protected UserWriteService serviceImpl() {
        return new UserWriteServiceImpl(vertx.getOrCreateContext());
    }
}


