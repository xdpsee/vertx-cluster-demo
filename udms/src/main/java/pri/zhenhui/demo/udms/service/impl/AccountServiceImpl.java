package pri.zhenhui.demo.udms.service.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.SingleHelper;
import io.vertx.reactivex.core.Context;
import pri.zhenhui.demo.udms.domain.User;
import pri.zhenhui.demo.udms.manager.AccountManager;
import pri.zhenhui.demo.udms.service.AccountService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class AccountServiceImpl implements AccountService {

    private final Context context;

    private final AccountManager accountManager;

    public AccountServiceImpl(Context context) {
        this.context = context;
        this.accountManager = AccountManager.getInstance();
    }

    @Override
    public void queryUserById(Long userId, Handler<AsyncResult<User>> resultHandler) {
        accountManager.getUserById(userId)
                .map(Optional::get)
                .subscribe(SingleHelper.toObserver(resultHandler));
    }

    @Override
    public void queryUserByIds(Set<Long> userIds, Handler<AsyncResult<List<User>>> resultHandler) {
        accountManager.getUserByIds(userIds)
                .subscribe(SingleHelper.toObserver(resultHandler));
    }

    @Override
    public void queryUserByName(String username, Handler<AsyncResult<User>> resultHandler) {

        accountManager.getUserByName(username)
                .map(Optional::get)
                .subscribe(SingleHelper.toObserver(resultHandler));
    }

    @Override
    public void queryUserByPhone(String phone, Handler<AsyncResult<User>> resultHandler) {

        accountManager.getUserByPhone(phone)
                .map(Optional::get)
                .subscribe(SingleHelper.toObserver(resultHandler));

    }

    @Override
    public void queryUsers(Long parentId, Handler<AsyncResult<List<User>>> resultHandler) {
        accountManager.queryUsers(parentId)
                .subscribe(SingleHelper.toObserver(resultHandler));
    }

    @Override
    public void createUser(User user, Handler<AsyncResult<Long>> resultHandler) {
        accountManager.createUser(user)
                .subscribe(SingleHelper.toObserver(resultHandler));
    }

    @Override
    public void updateUser(JsonObject fields, Handler<AsyncResult<Boolean>> resultHandler) {

        accountManager.updateUser(fields)
                .subscribe(SingleHelper.toObserver(resultHandler));

    }
}

