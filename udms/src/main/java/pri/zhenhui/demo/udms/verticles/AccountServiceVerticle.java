package pri.zhenhui.demo.udms.verticles;

import pri.zhenhui.demo.udms.service.AccountService;
import pri.zhenhui.demo.udms.service.impl.AccountServiceImpl;
import pri.zhenhui.demo.support.microservice.AbstractMicroServiceVerticle;

public class AccountServiceVerticle extends AbstractMicroServiceVerticle<AccountService> {

    public AccountServiceVerticle() {
        super(AccountService.SERVICE_NAME, AccountService.SERVICE_ADDRESS, AccountService.class);
    }

    @Override
    protected AccountService serviceImpl() {
        return new AccountServiceImpl(vertx.getOrCreateContext());
    }
}

