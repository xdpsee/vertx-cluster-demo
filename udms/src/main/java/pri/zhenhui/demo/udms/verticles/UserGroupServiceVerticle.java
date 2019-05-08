package pri.zhenhui.demo.udms.verticles;

import pri.zhenhui.demo.support.microservice.AbstractMicroServiceVerticle;
import pri.zhenhui.demo.udms.service.UserGroupService;
import pri.zhenhui.demo.udms.service.impl.UserGroupServiceImpl;

public class UserGroupServiceVerticle extends AbstractMicroServiceVerticle<UserGroupService> {

    public UserGroupServiceVerticle() {
        super(UserGroupService.SERVICE_NAME, UserGroupService.SERVICE_ADDRESS, UserGroupService.class);
    }

    @Override
    protected UserGroupService serviceImpl() {
        return new UserGroupServiceImpl(vertx.getOrCreateContext());
    }
}

