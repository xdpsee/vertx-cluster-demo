package pri.zhenhui.demo.webapi.handlers.security;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jwt.JWTOptions;
import io.vertx.reactivex.ext.web.RoutingContext;
import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;
import pri.zhenhui.demo.account.AccountService;
import pri.zhenhui.demo.account.AuthorityService;
import pri.zhenhui.demo.account.domain.Authority;
import pri.zhenhui.demo.account.domain.User;
import pri.zhenhui.demo.webapi.support.AbstractHandler;
import pri.zhenhui.demo.webapi.support.AppContext;
import pri.zhenhui.demo.webapi.support.Result;

import java.util.stream.Collectors;

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

        final AccountService accountService = appContext.getService(AccountService.SERVICE_NAME
                , AccountService.SERVICE_ADDRESS
                , AccountService.class);
        final AuthorityService authorityService = appContext.getService(AuthorityService.SERVICE_NAME
                , AuthorityService.SERVICE_ADDRESS
                , AuthorityService.class);

        accountService.queryUserByName(username, queryUserByName -> {
            if (queryUserByName.failed()) {
                write(context, Result.error(500, "internal server error", queryUserByName.cause()));
                return;
            }

            final User user = queryUserByName.result();
            if (user == null) {
                write(context, Result.error(404, "User Not Found"));
                return;
            }

            if (!BCrypt.checkpw(password, queryUserByName.result().getPassword())) {
                write(context, Result.error(401, "Password Mismatch"));
                return;
            }

            authorityService.queryUserAuthorities(user.getId(), queryUserAuthorities -> {
                if (queryUserAuthorities.failed()) {
                    write(context, Result.error(500, "Service Error", queryUserAuthorities.cause()));
                    return;
                }

                this.<String>executeBlocking(future -> {
                    try {
                        JsonObject claims = new JsonObject().put("sub", username).put("uid", user.getId());
                        JWTOptions options = new JWTOptions()
                                .setPermissions(queryUserAuthorities.result()
                                        .stream()
                                        .map(Authority::getTitle)
                                        .collect(Collectors.toList())
                                );
                        future.complete(appContext.jwtAuth().generateToken(claims, options));
                    } catch (Throwable e) {
                        future.fail(e);
                    }
                }, generateToken -> {
                    if (generateToken.failed()) {
                        write(context, Result.error(500, "Service Error", generateToken.cause()));
                    } else {
                        write(context, Result.success(generateToken.result()));
                    }
                });
            });
        });
    }
}

