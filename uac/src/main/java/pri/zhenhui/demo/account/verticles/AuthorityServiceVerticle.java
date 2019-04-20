package pri.zhenhui.demo.account.verticles;

import pri.zhenhui.demo.account.service.AuthorityService;
import pri.zhenhui.demo.account.service.impl.AuthorityServiceImpl;
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
