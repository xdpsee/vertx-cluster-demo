package pri.zhenhui.demo.tracer.website.handlers;

import io.vertx.reactivex.ext.web.RoutingContext;
import pri.zhenhui.demo.tracer.website.support.AbstractHandler;
import pri.zhenhui.demo.tracer.website.support.AppContext;

import java.util.HashMap;

/**
 * @author zhenhui
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public class IndexHandler extends AbstractHandler {

    public IndexHandler(AppContext appContext) {
        super(appContext);
    }

    @Override
    public void handle(RoutingContext context) {
        rxRender(new HashMap<>(8), "webroot/templates/index.html")
                .subscribe(buffer -> {
                    context.response()
                            .putHeader("Content-Type", "text/html")
                            .end(buffer);
                }, err -> {
                    context.response()
                            .setStatusCode(404)
                            .end();
                });
    }
}



