package pri.zhenhui.demo.webapi.handlers.security;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.impl.JWTUser;
import io.vertx.ext.web.RoutingContext;
import org.apache.commons.lang3.StringUtils;
import pri.zhenhui.demo.webapi.support.AbstractHandler;
import pri.zhenhui.demo.webapi.support.AppContext;
import pri.zhenhui.demo.webapi.support.Result;

public class AuthenticationHandler extends AbstractHandler {

    private static final String AUTHORIZATION = "Authorization";

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
            if (components.length != 2) {
                throw new RuntimeException("Invalid Authorization Header ->  Authorization: " + header);
            }

            appContext.jwtAuth().authenticate(new JsonObject().put("jwt", components[1]), authenticate -> {
                if (authenticate.succeeded()) {
                    JWTUser user = (JWTUser) authenticate.result();
                    context.setUser(user);
                    context.next();
                } else {
                    authenticate.cause().printStackTrace();
                    Result.error(401, "Authenticate Failed", authenticate.cause());
                }
            });
        }
    }
}


