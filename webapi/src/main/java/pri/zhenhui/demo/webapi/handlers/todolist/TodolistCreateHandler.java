package pri.zhenhui.demo.webapi.handlers.todolist;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.servicediscovery.ServiceDiscovery;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import pri.zhenhui.demo.todolist.Todolist;
import pri.zhenhui.demo.todolist.TodolistDataService;

public class TodolistCreateHandler extends AbstractHandler {

    public TodolistCreateHandler(ServiceDiscovery serviceDiscovery) {
        super(serviceDiscovery);
    }

    @Override
    public void handle(RoutingContext context) {

        try {
            String title = context.request().getFormAttribute("title");
            if (StringUtils.isBlank(title)) {
                context.response().setStatusCode(400).end();
                return;
            }

            TodolistDataService service = getService(TodolistDataService.SERVICE_NAME
                    , TodolistDataService.SERVICE_ADDRESS
                    , TodolistDataService.class);

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



