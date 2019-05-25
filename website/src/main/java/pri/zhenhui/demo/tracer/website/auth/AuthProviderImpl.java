package pri.zhenhui.demo.tracer.website.auth;


import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;
import pri.zhenhui.demo.tracer.website.support.AppContext;
import pri.zhenhui.demo.udms.domain.UserToken;
import pri.zhenhui.demo.udms.service.UserTokenService;

public class AuthProviderImpl implements AuthProvider {

    private final AppContext appContext;

    public AuthProviderImpl(AppContext appContext) {
        this.appContext = appContext;
    }

    @Override
    public void authenticate(JsonObject jsonObj, Handler<AsyncResult<User>> handler) {
        final String username = jsonObj.getString("username");
        final String password = jsonObj.getString("password");

        final UserTokenService userTokenService = appContext.getService(UserTokenService.SERVICE_NAME
                , UserTokenService.SERVICE_ADDRESS
                , UserTokenService.class);

        userTokenService.generateToken(username, password, generateToken -> {
            if (generateToken.succeeded()) {
                UserToken userToken = generateToken.result();
                handler.handle(Future.succeededFuture(new UserImpl(userToken.getPrincipal())));
            } else {
                handler.handle(Future.failedFuture(generateToken.cause()));
            }
        });
    }
}

