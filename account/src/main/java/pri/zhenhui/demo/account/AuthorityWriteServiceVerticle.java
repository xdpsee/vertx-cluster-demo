package pri.zhenhui.demo.account;

import pri.zhenhui.demo.support.AbstractMicroServiceVerticle;

public class AuthorityWriteServiceVerticle extends AbstractMicroServiceVerticle<AuthorityWriteService> {

    public AuthorityWriteServiceVerticle() {
        super(AuthorityWriteService.SERVICE_NAME, AuthorityWriteService.SERVICE_ADDRESS, AuthorityWriteService.class);
    }

    @Override
    protected AuthorityWriteService serviceImpl() {
        return new AuthorityWriteServiceImpl(vertx.getOrCreateContext());
    }
}

