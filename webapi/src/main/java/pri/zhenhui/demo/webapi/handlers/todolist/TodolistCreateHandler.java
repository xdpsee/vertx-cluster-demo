package pri.zhenhui.demo.webapi.handlers.todolist;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import pri.zhenhui.demo.todolist.TodolistService;
import pri.zhenhui.demo.todolist.domain.Todolist;
import pri.zhenhui.demo.webapi.support.AbstractHandler;
import pri.zhenhui.demo.webapi.support.AppContext;

public class TodolistCreateHandler extends AbstractHandler {

    public TodolistCreateHandler(AppContext appContext) {
        super(appContext);
    }

    @Override
    public void handle(RoutingContext context) {

        try {
            String title = context.request().getFormAttribute("title");
            if (StringUtils.isBlank(title)) {
                context.response().setStatusCode(400).end();
                return;
            }

            TodolistService service = appContext.getService(TodolistService.SERVICE_NAME
                    , TodolistService.SERVICE_ADDRESS
                    , TodolistService.class);

            Todolist todolist = new Todolist(new JsonObject()
                    .put("id", RandomStringUtils.randomAlphanumeric(32))
                    .put("title", title)
                    .put("status", "TODO"));

            service.createTodo(todolist, createTodo -> {
                if (createTodo.failed()) {
                    createTodo.cause().printStackTrace();
                    context.response().setStatusCode(500).end();
                } else {
                    context.response().end();
                }
            });

        } catch (Exception e) {
            context.response().setStatusCode(500).end();
        }

    }
}



