package pri.zhenhui.demo.uac.verticles;

import pri.zhenhui.demo.uac.service.AuthorityService;
import pri.zhenhui.demo.uac.service.impl.AuthorityServiceImpl;
import pri.zhenhui.demo.support.microservice.AbstractMicroServiceVerticle;

public class AuthorityServiceVerticle extends AbstractMicroServiceVerticle<AuthorityService> {

    public AuthorityServiceVerticle() {
        super(AuthorityService.SERVICE_NAME, AuthorityService.SERVICE_ADDRESS, AuthorityService.class);
    }

    @Override
    protected AuthorityService serviceImpl() {
        return new AuthorityServiceImpl(vertx.getOrCreateContext());
    }
}
