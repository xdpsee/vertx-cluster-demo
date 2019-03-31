package pri.zhenhui.demo.todolist;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import pri.zhenhui.demo.todolist.domain.Todolist;

import java.util.List;

@VertxGen
@ProxyGen
public interface TodolistService {

    String SERVICE_NAME = "service.data.todolist";

    String SERVICE_ADDRESS = "address.service.data.todolist";

    void listTodoes(Long userId, int offset, int limit, Handler<AsyncResult<List<Todolist>>> resultHandler);

    void createTodo(Todolist todolist, Handler<AsyncResult<Boolean>> resultHandler);

    void updateTodo(String todoId, String title, String status, Handler<AsyncResult<Boolean>> resultHandler);
}

