package pri.zhenhui.demo.webapi.handlers.todolist;

import io.reactivex.Single;
import io.vertx.reactivex.ext.web.RoutingContext;
import org.apache.commons.lang3.StringUtils;
import pri.zhenhui.demo.todolist.TodolistService;
import pri.zhenhui.demo.todolist.domain.Status;
import pri.zhenhui.demo.todolist.domain.Todolist;
import pri.zhenhui.demo.udms.domain.enums.AuthorityType;
import pri.zhenhui.demo.webapi.exception.PermissionException;
import pri.zhenhui.demo.webapi.support.AbstractHandler;
import pri.zhenhui.demo.webapi.support.AppContext;
import pri.zhenhui.demo.webapi.support.AuthUtils;
import pri.zhenhui.demo.webapi.support.Result;

import java.util.Date;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class TodolistCreateHandler extends AbstractHandler {

    public TodolistCreateHandler(AppContext appContext) {
        super(appContext);
    }

    @Override
    public void handle(RoutingContext context) {

        final String title = context.request().getFormAttribute("title");
        if (StringUtils.isBlank(title)) {
            write(context, Result.error(400, "Bad Request"));
            return;
        }

        final Todolist todolist = new Todolist();
        todolist.setTitle(title);
        todolist.setStatus(Status.STATUS_TODO);
        todolist.setCreateAt(new Date());
        todolist.setUpdateAt(new Date());

        context.user()
                .rxIsAuthorized(AuthorityType.TODOLIST_CREATE.title)
                .doOnSuccess(success -> {
                    if (success) {
                        todolist.setUserId(AuthUtils.userId(context));
                    }
                })
                .flatMap(success -> !success
                        ? Single.error(new PermissionException())
                        : Single.create(emitter -> {
                    TodolistService service = appContext.getService(TodolistService.SERVICE_NAME
                            , TodolistService.SERVICE_ADDRESS
                            , TodolistService.class);
                    service.createTodo(todolist, createTodo -> {
                        if (createTodo.failed()) {
                            emitter.onError(createTodo.cause());
                        } else {
                            emitter.onSuccess(createTodo.result());
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



