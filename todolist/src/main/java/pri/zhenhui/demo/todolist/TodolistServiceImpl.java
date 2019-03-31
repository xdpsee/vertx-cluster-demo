package pri.zhenhui.demo.todolist;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import pri.zhenhui.demo.todolist.domain.Todolist;
import pri.zhenhui.demo.todolist.mapper.TodolistMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TodolistServiceImpl implements TodolistService {

    private Vertx vertx;

    private SqlSessionFactory sqlSessionFactory;

    public TodolistServiceImpl(Vertx vertx, SqlSessionFactory sqlSessionFactory) {
        this.vertx = vertx;
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public void listTodoes(Long userId, int offset, int limit, Handler<AsyncResult<List<Todolist>>> resultHandler) {

        vertx.<List<Todolist>>executeBlocking(future -> {
            SqlSession session = sqlSessionFactory.openSession();
            try {
                TodolistMapper todolistMapper = session.getMapper(TodolistMapper.class);
                List<Todolist> result = todolistMapper.selectByPage(userId, offset, limit);
                future.complete(result);
            } catch (Exception e) {
                future.fail(e);
            } finally {
                session.close();
            }
        }, resultHandler);

    }

    @Override
    public void createTodo(Todolist todolist, Handler<AsyncResult<Boolean>> resultHandler) {

        vertx.<Boolean>executeBlocking(future -> {
            SqlSession session = sqlSessionFactory.openSession();
            try {
                TodolistMapper todolistMapper = session.getMapper(TodolistMapper.class);
                int rows = todolistMapper.insert(todolist);
                future.complete(rows == 1);
            } catch (Exception e) {
                future.fail(e);
            } finally {
                session.close();
            }
        }, resultHandler);

    }

    @Override
    public void updateTodo(String todoId, String title, String status, Handler<AsyncResult<Boolean>> resultHandler) {
        vertx.<Boolean>executeBlocking(future -> {
            SqlSession session = sqlSessionFactory.openSession();
            try {
                TodolistMapper todolistMapper = session.getMapper(TodolistMapper.class);

                Map<String, Object> params = new HashMap<>();
                params.put("id", todoId);
                params.put("title", title);
                params.put("status", status);

                int rows = todolistMapper.update(params);
                future.complete(rows == 1);
            } catch (Exception e) {
                future.fail(e);
            } finally {
                session.close();
            }
        }, resultHandler);

    }
}
