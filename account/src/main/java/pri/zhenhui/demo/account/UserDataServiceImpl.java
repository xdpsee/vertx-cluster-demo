package pri.zhenhui.demo.account;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

public class UserDataServiceImpl implements UserDataService {

    @Override
    public void createUser(User user, Handler<AsyncResult<Boolean>> resultHandler) {
    }

    @Override
    public void updateUser(Long userId, JsonObject fields, Handler<AsyncResult<Boolean>> resultHandler) {
    }

    @Override
    public void queryUserByName(String username, Handler<AsyncResult<User>> resultHandler) {

    }

    @Override
    public void queryUserByPhone(String phone, Handler<AsyncResult<User>> resultHandler) {

    }
}
