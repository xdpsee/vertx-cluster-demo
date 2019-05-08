package pri.zhenhui.demo.udms.service;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import pri.zhenhui.demo.udms.domain.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

@ProxyGen
@VertxGen
public interface AccountService {

    String SERVICE_NAME = "service.data.user";

    String SERVICE_ADDRESS = "address.service.data.user";

    void queryUserById(Long userId, Handler<AsyncResult<User>> resultHandler);

    void queryUserByIds(Set<Long> userIds, Handler<AsyncResult<List<User>>> resultHandler);

    void queryUserByName(String username, Handler<AsyncResult<User>> resultHandler);

    void queryUserByPhone(String phone, Handler<AsyncResult<User>> resultHandler);

    void queryUsers(Long parentId, Handler<AsyncResult<List<User>>> resultHandler);

    void createUser(User user, Handler<AsyncResult<Long>> resultHandler);

    void updateUser(JsonObject fields, Handler<AsyncResult<Boolean>> resultHandler);

}
