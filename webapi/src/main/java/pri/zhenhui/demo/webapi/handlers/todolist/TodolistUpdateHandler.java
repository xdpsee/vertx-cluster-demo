package pri.zhenhui.demo.webapi.handlers.todolist;

import io.reactivex.Single;
import io.vertx.reactivex.ext.web.RoutingContext;
import org.apache.commons.lang3.StringUtils;
import pri.zhenhui.demo.todolist.TodolistService;
import pri.zhenhui.demo.todolist.domain.Status;
import pri.zhenhui.demo.udms.domain.enums.AuthorityType;
import pri.zhenhui.demo.webapi.exception.PermissionException;
import pri.zhenhui.demo.webapi.support.AbstractHandler;
import pri.zhenhui.demo.webapi.support.AppContext;
import pri.zhenhui.demo.webapi.support.Result;

public class TodolistUpdateHandler extends AbstractHandler {

    public TodolistUpdateHandler(AppContext appContext) {
        super(appContext);
    }

    @Override
    public void handle(RoutingContext context) {

        final String id = context.request().getParam("id");
        final String title = context.request().getFormAttribute("title");
        final String status = context.request().getFormAttribute("status");
        if (StringUtils.isBlank(id)
                || (StringUtils.isBlank(title) && StringUtils.isBlank(status))
                || (!StringUtils.isBlank(status) && Status.accept(status))) {
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
                    if (error instanceof PermissionException) {
                        write(context, Result.error(403, "Access Forbidden"));
                    } else {
                        write(context, Result.error(500, "Service Error", error));
                    }
                });

    }
}
