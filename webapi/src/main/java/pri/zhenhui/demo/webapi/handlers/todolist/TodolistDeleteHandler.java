package pri.zhenhui.demo.webapi.handlers.todolist;

import io.reactivex.Single;
import io.vertx.reactivex.ext.web.RoutingContext;
import org.apache.commons.lang3.StringUtils;
import pri.zhenhui.demo.todolist.TodolistService;
import pri.zhenhui.demo.udms.domain.enums.AuthorityType;
import pri.zhenhui.demo.webapi.exception.PermissionException;
import pri.zhenhui.demo.webapi.support.AbstractHandler;
import pri.zhenhui.demo.webapi.support.AppContext;
import pri.zhenhui.demo.webapi.support.Result;

public class TodolistDeleteHandler extends AbstractHandler {

    public TodolistDeleteHandler(AppContext appContext) {
        super(appContext);
    }

    @Override
    public void handle(RoutingContext context) {

        final String id = context.request().getParam("id");
        if (!StringUtils.isNumeric(id)) {
            write(context, Result.error(400, "Bad Request"));
            return;
        }

        final TodolistService service = appContext.getService(TodolistService.SERVICE_NAME
                , TodolistService.SERVICE_ADDRESS
                , TodolistService.class);

        context.user()
                .rxIsAuthorized(AuthorityType.TODOLIST_DELETE.title)
                .flatMap(success -> !success
                        ? Single.error(new PermissionException())
                        : Single.<Boolean>create(emitter -> {
                    service.deleteTodo(id, delete -> {
                        if (delete.failed()) {
                            emitter.onError(delete.cause());
                        } else {
                            emitter.onSuccess(delete.result());
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
