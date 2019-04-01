package pri.zhenhui.demo.webapi.handlers.security;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;
import pri.zhenhui.demo.account.UserReadService;
import pri.zhenhui.demo.webapi.support.AbstractHandler;
import pri.zhenhui.demo.webapi.support.AppContext;
import pri.zhenhui.demo.webapi.support.Result;

public class LoginHandler extends AbstractHandler {

    public LoginHandler(AppContext appContext) {
        super(appContext);
    }

    @Override
    public void handle(RoutingContext context) {

        String username = context.request().getParam("username");
        String password = context.request().getParam("password");

        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            context.response().end(Json.encode(Result.error(400, "username and password shouldn't be empty")));
            return;
        }

        final UserReadService userReadService = appContext.getService(UserReadService.SERVICE_NAME
                , UserReadService.SERVICE_ADDRESS
                , UserReadService.class);

        userReadService.queryUserByName(username, query -> {
            if (query.failed()) {
                context.response().end(Json.encode(Result.error(500, "internal server error", query.cause())));
            } else {
                if (query.result() == null) {
                    context.response().end(Json.encode(Result.error(404, "user not found")));
                } else {
                    if (!BCrypt.checkpw(password, query.result().getPassword())) {
                        context.response().end(Json.encode(Result.error(401, "password mismatch")));
                    } else {
                        String token = appContext.jwtAuth().generateToken(new JsonObject()
                                .put("sub", username)
                        );
                        context.response().end(Json.encode(Result.success(token)));
                    }
                }
            }
        });
    }
}

