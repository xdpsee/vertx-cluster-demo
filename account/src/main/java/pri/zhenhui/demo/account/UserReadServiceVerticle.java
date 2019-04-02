package pri.zhenhui.demo.account;

import pri.zhenhui.demo.support.AbstractMicroServiceVerticle;

public class UserReadServiceVerticle extends AbstractMicroServiceVerticle<UserReadService> {

    public UserReadServiceVerticle() {
        super(UserReadService.SERVICE_NAME, UserReadService.SERVICE_ADDRESS, UserReadService.class);
    }

    @Override
    protected UserReadService serviceImpl() {
        return new UserReadServiceImpl(vertx.getOrCreateContext());
    }
}

