package pri.zhenhui.demo.webapi.support;

import io.vertx.reactivex.ext.web.RoutingContext;

public class AuthUtils {

    public static Long userId(RoutingContext context) {
        return context.user().principal().getLong("uid");
    }

    public static String username(RoutingContext context) {
        return context.user().principal().getString("sub");
    }

}
