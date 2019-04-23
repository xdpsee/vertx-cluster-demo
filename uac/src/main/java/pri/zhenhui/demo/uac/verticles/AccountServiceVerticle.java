package pri.zhenhui.demo.uac.verticles;

import pri.zhenhui.demo.uac.service.AccountService;
import pri.zhenhui.demo.uac.service.impl.AccountServiceImpl;
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

