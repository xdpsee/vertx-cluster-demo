package pri.zhenhui.demo.webapi.handlers.security;

import io.vertx.ext.auth.jwt.impl.JWTUser;
import io.vertx.reactivex.ext.auth.User;
import io.vertx.reactivex.ext.web.RoutingContext;
import org.apache.commons.lang3.StringUtils;
import pri.zhenhui.demo.udms.service.UserTokenService;
import pri.zhenhui.demo.webapi.support.AbstractHandler;
import pri.zhenhui.demo.webapi.support.AppContext;
import pri.zhenhui.demo.webapi.support.Result;

public class AuthenticationHandler extends AbstractHandler {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer";

    public AuthenticationHandler(AppContext appContext) {
        super(appContext);
    }

    @Override
    public void handle(RoutingContext context) {

        final String header = context.request().getHeader(AUTHORIZATION);
        if (context.user() != null || StringUtils.isBlank(header)) {
            context.next();

        } else {
            String[] components = header.split("\\s");
            if (components.length != 2 || !BEARER.equals(components[0])) {
                throw new RuntimeException("Invalid Authorization Header ->  Authorization: " + header);
            }

            final UserTokenService userTokenService = appContext.getService(UserTokenService.SERVICE_NAME
                    , UserTokenService.SERVICE_ADDRESS
                    , UserTokenService.class);

            userTokenService.validateToken(components[1], authenticate -> {
                if (authenticate.succeeded()) {
                    JWTUser jwtUser = new JWTUser(authenticate.result(), "permissions");
                    context.setUser(new User(jwtUser));
                    context.next();
                } else {
                    write(context, Result.error(401, "Authenticate Failed", authenticate.cause()));
                }
            });
        }
    }
}




