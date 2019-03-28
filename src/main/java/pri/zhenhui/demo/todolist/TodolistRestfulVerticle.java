package pri.zhenhui.demo.todolist;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.types.EventBusService;
import org.apache.commons.lang3.StringUtils;

public class TodolistRestfulVerticle extends AbstractVerticle {

    private static final String KEY_PORT = "port";

    private TodolistDataService todolistDataService;

    private ServiceDiscovery discovery;

    @Override
    public void init(Vertx vertx, Context context) {

        super.init(vertx, context);

        discovery = ServiceDiscovery.create(vertx, r -> {
            ServiceReference reference = r.getReference(EventBusService.createRecord(TodolistDataService.SERVICE_NAME, TodolistDataService.SERVICE_ADDRESS, TodolistDataService.class));
            todolistDataService = reference.getAs(TodolistDataService.class);
            reference.release();
        });

    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {

        final Integer port = config().getInteger(KEY_PORT, 8080);

        vertx.createHttpServer()
                .requestHandler(createRouter())
                .listen(port, ar -> {
                    if (ar.failed()) {
                        startFuture.fail(ar.cause());
                    } else {
                        startFuture.complete();
                    }
                });
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {

        discovery.close();

        stopFuture.complete();
    }

    private Router createRouter() {

        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        router.get("/todolist").handler(context -> {
            todolistDataService.listTodoes(listTodoes -> {
                if (listTodoes.failed()) {
                    context.response().setStatusCode(500).end("Internal Server Error");
                } else {
                    context.response().putHeader("Content-Type", "application/json;charset=utf-8").end(Json.encode(listTodoes.result()));
                }
            });
        });

        router.post("/todolist").handler(context -> {
            String title = context.request().getFormAttribute("title");
            Todolist todolist = new Todolist(new JsonObject().put("title", title));
            todolistDataService.createTodo(todolist, createTodo -> {
                if (createTodo.failed()) {
                    createTodo.cause().printStackTrace();
                    context.response().setStatusCode(500).end("Internal Server Error");
                } else {
                    context.response().end(createTodo.result().toString());
                }
            });
        });

        router.delete("/todolist/:todoId").handler(context -> {
            String id = context.request().getParam("todoId");
            if (StringUtils.isBlank(id)) {
                context.response().setStatusCode(400).end("BAD REQUEST");
            } else {
                todolistDataService.removeTodo(id, removeTodo -> {
                    if (removeTodo.failed()) {
                        removeTodo.cause().printStackTrace();
                        context.response().setStatusCode(500).end("Internal Server Error");
                    } else {
                        context.response().end(removeTodo.result().toString());
                    }
                });
            }
        });

        router.put("/todolist/:todoId").handler(context -> {
            String id = context.request().getParam("todoId");
            if (StringUtils.isBlank(id)) {
                context.response().setStatusCode(400).end("BAD REQUEST");
            } else {
                String status = context.request().getFormAttribute("status");
                todolistDataService.updateTodo(id, status, updateTodo -> {
                    if (updateTodo.failed()) {
                        updateTodo.cause().printStackTrace();
                        context.response().setStatusCode(500).end("Internal Server Error");
                    } else {
                        context.response().end(updateTodo.result().toString());
                    }
                });
            }
        });

        return router;
    }
}


