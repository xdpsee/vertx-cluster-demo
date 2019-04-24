package pri.zhenhui.demo.uac.service;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import pri.zhenhui.demo.uac.domain.User;

@ProxyGen
@VertxGen
public interface AccountService {

    String SERVICE_NAME = "service.data.user";

    String SERVICE_ADDRESS = "address.service.data.user";

    void queryUserById(Long userId, Handler<AsyncResult<User>> resultHandler);

    void queryUserByName(String username, Handler<AsyncResult<User>> resultHandler);

    void queryUserByPhone(String phone, Handler<AsyncResult<User>> resultHandler);

    void createUser(User user, Handler<AsyncResult<Long>> resultHandler);

    void updateUser(JsonObject fields, Handler<AsyncResult<Boolean>> resultHandler);
}
