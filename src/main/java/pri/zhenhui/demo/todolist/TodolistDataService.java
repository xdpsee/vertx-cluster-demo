package pri.zhenhui.demo.todolist;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.types.EventBusService;

import java.util.List;

@SuppressWarnings("unused")
@VertxGen
@ProxyGen
public interface TodolistDataService {

    String SERVICE_NAME = "todolist.data.service";

    String SERVICE_ADDRESS = "address.todolist.data.service";

    Record RECORD = EventBusService.createRecord(SERVICE_NAME, SERVICE_ADDRESS, TodolistDataService.class);

    static TodolistDataService createProxy(Vertx vertx, String address, DeliveryOptions deliveryOptions) {
        return new TodolistDataServiceVertxEBProxy(vertx, address, deliveryOptions);
    }

    @Fluent
    TodolistDataService listTodoes(Handler<AsyncResult<List<Todolist>>> handler);

    @Fluent
    TodolistDataService createTodo(Todolist todolist, Handler<AsyncResult<Boolean>> handler);

    @Fluent
    TodolistDataService removeTodo(String todoId, Handler<AsyncResult<Boolean>> handler);

    @Fluent
    TodolistDataService updateTodo(String todoId, String status, Handler<AsyncResult<Boolean>> handler);
}

