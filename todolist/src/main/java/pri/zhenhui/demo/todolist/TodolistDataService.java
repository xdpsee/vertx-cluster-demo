package pri.zhenhui.demo.todolist;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

import java.util.List;

@VertxGen
@ProxyGen
public interface TodolistDataService {

    String SERVICE_NAME = "todolist.data.service";

    String SERVICE_ADDRESS = "address.todolist.data.service";

    void listTodoes(Handler<AsyncResult<List<Todolist>>> handler);

    void createTodo(Todolist todolist, Handler<AsyncResult<Boolean>> handler);

    void removeTodo(String todoId, Handler<AsyncResult<Boolean>> handler);

    void updateTodo(String todoId, String status, Handler<AsyncResult<Boolean>> handler);
}

