package pri.zhenhui.demo.webapi.handlers.security;

import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.serviceproxy.ServiceException;
import org.apache.commons.lang3.StringUtils;
import pri.zhenhui.demo.udms.service.UserTokenService;
import pri.zhenhui.demo.udms.service.exception.Errors;
import pri.zhenhui.demo.webapi.support.AbstractHandler;
import pri.zhenhui.demo.webapi.support.AppContext;
import pri.zhenhui.demo.webapi.support.Result;

public class LoginHandler extends AbstractHandler {

    public LoginHandler(AppContext appContext) {
        super(appContext);
    }

    @Override
    public void handle(RoutingContext context) {

        final String username = context.request().getParam("username");
        final String password = context.request().getParam("password");
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            write(context, Result.error(400, "Username or password shouldn't be empty"));
            return;
        }

        final UserTokenService userTokenService = appContext.getService(UserTokenService.SERVICE_NAME
                , UserTokenService.SERVICE_ADDRESS
                , UserTokenService.class);

        userTokenService.generateToken(username, password, result -> {
            if (result.succeeded()) {
                write(context, Result.success(result.result()));
            } else {
                ServiceException exception = (ServiceException) result.cause();
                if (exception.failureCode() == Errors.USER_NOT_FOUND.code) {
                    write(context, Result.error(404, "User Not Found"));
                } else if (exception.failureCode() == Errors.USER_PASSWORD_MISMATCH.code) {
                    write(context, Result.error(401, "Password Mismatch"));
                } else {
                    write(context, Result.error(500, "Service Error", result.cause()));
                }
            }
        });
    }
}

