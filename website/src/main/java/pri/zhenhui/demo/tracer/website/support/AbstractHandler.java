package pri.zhenhui.demo.tracer.website.support;

import io.reactivex.Single;
import io.vertx.core.Handler;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.ext.web.RoutingContext;

import java.util.Map;

/**
 * @author zhenhui
 */
public abstract class AbstractHandler implements Handler<RoutingContext> {

    private final AppContext appContext;

    protected AbstractHandler(AppContext appContext) {
        this.appContext = appContext;
    }

    protected Single<Buffer> rxRender(Map<String, Object> context, String templateFileName) {
        return appContext.templateEngine()
                .rxRender(context, templateFileName)
                .subscribeOn(appContext.scheduler());
    }
}

