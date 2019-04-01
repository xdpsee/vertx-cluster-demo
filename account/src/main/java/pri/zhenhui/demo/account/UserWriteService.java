package pri.zhenhui.demo.account;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import pri.zhenhui.demo.account.domain.User;

@ProxyGen
@VertxGen
public interface UserWriteService {

    String SERVICE_NAME = "service.data.user.write";

    String SERVICE_ADDRESS = "address.service.data.user.write";

    void createUser(User user, Handler<AsyncResult<Boolean>> resultHandler);

    void updateUser(JsonObject fields, Handler<AsyncResult<Boolean>> resultHandler);

}
