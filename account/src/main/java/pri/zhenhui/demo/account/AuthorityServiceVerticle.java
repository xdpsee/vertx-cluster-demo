package pri.zhenhui.demo.account;

import pri.zhenhui.demo.support.AbstractMicroServiceVerticle;

public class AuthorityServiceVerticle extends AbstractMicroServiceVerticle<AuthorityService> {

    public AuthorityServiceVerticle() {
        super(AuthorityService.SERVICE_NAME, AuthorityService.SERVICE_ADDRESS, AuthorityService.class);
    }

    @Override
    protected AuthorityService serviceImpl() {
        return new AuthorityServiceImpl(vertx.getOrCreateContext());
    }
}
