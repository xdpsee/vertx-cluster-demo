package pri.zhenhui.demo.account;

import pri.zhenhui.demo.support.AbstractMicroServiceVerticle;

public class AuthorityReadServiceVerticle extends AbstractMicroServiceVerticle<AuthorityReadService> {

    public AuthorityReadServiceVerticle() {
        super(AuthorityReadService.SERVICE_NAME, AuthorityReadService.SERVICE_ADDRESS, AuthorityReadService.class);
    }

    @Override
    protected AuthorityReadService serviceImpl() {
        return new AuthorityReadServiceImpl(vertx.getOrCreateContext());
    }
}
