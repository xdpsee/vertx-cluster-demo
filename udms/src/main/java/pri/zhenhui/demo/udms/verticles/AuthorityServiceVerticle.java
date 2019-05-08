package pri.zhenhui.demo.udms.verticles;

import pri.zhenhui.demo.udms.service.AuthorityService;
import pri.zhenhui.demo.udms.service.impl.AuthorityServiceImpl;
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
