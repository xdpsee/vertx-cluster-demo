package pri.zhenhui.demo.webapi.handlers.todolist;

import io.reactivex.Single;
import io.vertx.reactivex.ext.web.RoutingContext;
import org.apache.ibatis.session.SqlSessionFactory;
import org.codehaus.plexus.util.StringUtils;
import pri.zhenhui.demo.account.domain.enums.AuthorityType;
import pri.zhenhui.demo.support.SqlSessionFactoryLoader;
import pri.zhenhui.demo.todolist.TodolistService;
import pri.zhenhui.demo.webapi.exception.PermissionException;
import pri.zhenhui.demo.webapi.support.AbstractHandler;
import pri.zhenhui.demo.webapi.support.AppContext;
import pri.zhenhui.demo.webapi.support.Result;

public class TodolistUpdateHandler extends AbstractHandler {

    private final SqlSessionFactory sqlSessionFactory;

    public TodolistUpdateHandler(AppContext appContext) {
        super(appContext);

        this.sqlSessionFactory = SqlSessionFactoryLoader.load();
    }

    @Override
    public void handle(RoutingContext context) {

        final String id = context.request().getParam("id");
        final String title = context.request().getFormAttribute("title");
        final String status = context.request().getFormAttribute("status");
        if (StringUtils.isBlank(id) || (StringUtils.isBlank(title) && StringUtils.isBlank(status))) {
            write(context, Result.error(400, "Bad Request"));
            return;
        }

        final TodolistService service = appContext.getService(TodolistService.SERVICE_NAME
                , TodolistService.SERVICE_ADDRESS
                , TodolistService.class);

        context.user()
                .rxIsAuthorized(AuthorityType.TODOLIST_CREATE.title)
                .flatMap(success -> !success
                        ? Single.error(new PermissionException())
                        : Single.<Boolean>create(emitter -> {
                    service.updateTodo(id, title, status, update -> {
                        if (update.failed()) {
                            emitter.onError(update.cause());
                        } else {
                            emitter.onSuccess(update.result());
                        }
                    });
                }))
                .subscribe(success -> {
                    write(context, Result.success(success));
                }, error -> {
                    write(context, Result.error(500, "Service Error", error));
                });

    }
}
