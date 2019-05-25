package pri.zhenhui.demo.tracer.website.pages;

import io.vertx.reactivex.ext.web.RoutingContext;
import org.thymeleaf.util.StringUtils;
import pri.zhenhui.demo.tracer.website.support.AbstractHandler;
import pri.zhenhui.demo.tracer.website.support.AppContext;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class LoginPage extends AbstractHandler {

    public LoginPage(AppContext appContext) {
        super(appContext);
    }

    @Override
    public void handle(RoutingContext context) {
        if (context.user() != null) {
            context.response()
                    .setStatusCode(302)
                    .putHeader("location", "/")
                    .end();
            return;
        }

        final Map<String, Object> params = new HashMap<>();
        String error = context.request().getParam("error");
        if (!StringUtils.isEmptyOrWhitespace(error)) {
            params.put("error", true);
            params.put("message", error);
        }

        rxRender(params, "webroot/templates/login.html")
                .subscribe(buffer -> context.response()
                                .putHeader("Content-Type", "text/html")
                                .end(buffer)
                        , err -> context.response()
                                .setStatusCode(404)
                                .end()
                );
    }
}

