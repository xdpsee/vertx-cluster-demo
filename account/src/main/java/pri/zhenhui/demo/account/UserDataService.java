package pri.zhenhui.demo.account;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

@VertxGen
@ProxyGen
public interface UserDataService {

    String SERVICE_NAME = "user.data.service";

    String SERVICE_ADDRESS = "address.user.data.service";

    void createUser(User user, Handler<AsyncResult<Boolean>> resultHandler);

    void updateUser(Long userId, JsonObject fields, Handler<AsyncResult<Boolean>> resultHandler);

    void queryUserByName(String username, Handler<AsyncResult<User>> resultHandler);

    void queryUserByPhone(String phone, Handler<AsyncResult<User>> resultHandler);
}

