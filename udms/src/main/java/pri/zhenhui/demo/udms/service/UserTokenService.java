package pri.zhenhui.demo.udms.service;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import pri.zhenhui.demo.udms.domain.UserToken;

@VertxGen
@ProxyGen
public interface UserTokenService {

    String SERVICE_NAME = "service.data.user.token";

    String SERVICE_ADDRESS = "address.service.data.user.token";

    /**
     *
     * @param username
     * @param password
     * @param resultHandler
     */
    void generateToken(String username, String password, Handler<AsyncResult<UserToken>> resultHandler);

    /**
     *
     * @param token
     * @param resultHandler
     */
    void validateToken(String token, Handler<AsyncResult<JsonObject>> resultHandler);

    /**
     *
     * @param token
     * @param resultHandler
     */
    void refreshToken(String token, Handler<AsyncResult<String>> resultHandler);

    /**
     *
     * @param token
     * @param resultHandler
     */
    void blockToken(String token, Handler<AsyncResult<Boolean>> resultHandler);
}

