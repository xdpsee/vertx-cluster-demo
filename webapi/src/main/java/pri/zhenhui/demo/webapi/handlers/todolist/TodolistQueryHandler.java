package pri.zhenhui.demo.webapi.handlers.todolist;

import io.reactivex.Single;
import io.vertx.reactivex.ext.web.RoutingContext;
import pri.zhenhui.demo.uac.domain.enums.AuthorityType;
import pri.zhenhui.demo.todolist.TodolistService;
import pri.zhenhui.demo.todolist.domain.Todolist;
import pri.zhenhui.demo.webapi.exception.PermissionException;
import pri.zhenhui.demo.webapi.support.AbstractHandler;
import pri.zhenhui.demo.webapi.support.AppContext;
import pri.zhenhui.demo.webapi.support.AuthUtils;
import pri.zhenhui.demo.webapi.support.Result;

import java.util.List;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class TodolistQueryHandler extends AbstractHandler {

    public TodolistQueryHandler(AppContext appContext) {
        super(appContext);
    }

    @Override
    public void handle(RoutingContext context) {

        final TodolistService service = appContext.getService(TodolistService.SERVICE_NAME
                , TodolistService.SERVICE_ADDRESS
                , TodolistService.class);

        context.user()
                .rxIsAuthorized(AuthorityType.TODOLIST_VIEW.title)
                .flatMap(success -> !success
                        ? Single.error(new PermissionException())
                        : Single.<List<Todolist>>create(emitter -> {
                    service.listTodoes(AuthUtils.userId(context), 0, 20, listTodoes -> {
                        if (listTodoes.failed()) {
                            emitter.onError(listTodoes.cause());
                        } else {
                            emitter.onSuccess(listTodoes.result());
                        }
                    });
                }))
                .subscribe(todoes -> {
                    write(context, Result.success(todoes));
                }, e -> {
                    if (e instanceof PermissionException) {
                        write(context, Result.error(403, "Access Forbidden"));
                    } else {
                        write(context, Result.error(500, "Service Error", e));
                    }
                });
    }

}
