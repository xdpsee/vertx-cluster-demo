package pri.zhenhui.demo.account;

import pri.zhenhui.demo.support.AbstractMicroServiceVerticle;

public class AccountServiceVerticle extends AbstractMicroServiceVerticle<AccountService> {

    public AccountServiceVerticle() {
        super(AccountService.SERVICE_NAME, AccountService.SERVICE_ADDRESS, AccountService.class);
    }

    @Override
    protected AccountService serviceImpl() {
        return new AccountServiceImpl(vertx.getOrCreateContext());
    }
}

