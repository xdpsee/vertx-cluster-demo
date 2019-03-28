package pri.zhenhui.demo.todolist;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.serviceproxy.ServiceException;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TodolistDataServiceImpl implements TodolistDataService {

    public static final Logger logger = LoggerFactory.getLogger(TodolistDataServiceImpl.class);

    private static final String INVALID_TODO = "INVALID_TODO";

    private JDBCClient jdbcClient;

    public TodolistDataServiceImpl(JDBCClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void listTodoes(Handler<AsyncResult<List<Todolist>>> handler) {

        jdbcClient.query(Constants.SQL_SELECT_ALL_TODOLIST, ar -> {
            if (ar.failed()) {
                handler.handle(Future.failedFuture(ar.cause()));
            } else {
                List<Todolist> todolist = ar.result()
                        .getResults()
                        .stream()
                        .map(e -> {
                            JsonObject jsonObj = new JsonObject();
                            jsonObj.put("id", e.getString(0));
                            jsonObj.put("title", e.getString(1));
                            jsonObj.put("status", e.getString(2));
                            Todolist todo = new Todolist(jsonObj);
                            return todo;
                        })
                        .collect(Collectors.toList());
                if (logger.isDebugEnabled()) {
                    logger.debug("listTodoes : " + Json.encode(todolist));
                }

                handler.handle(Future.succeededFuture(todolist));
            }
        });

    }

    @Override
    public void createTodo(Todolist todolist, Handler<AsyncResult<Boolean>> handler) {

        if (todolist == null || StringUtils.isBlank(todolist.getTitle())) {
            handler.handle(ServiceException.fail(400, INVALID_TODO));
            return;
        }

        todolist.setId(RandomStringUtils.randomAlphanumeric(32));
        todolist.setStatus(Todolist.STATUS_TODO);

        jdbcClient.getConnection(getConnection -> {
            if (getConnection.failed()) {
                handler.handle(Future.failedFuture(getConnection.cause()));
            } else {
                SQLConnection connection = getConnection.result();
                connection.updateWithParams(Constants.SQL_INSERT_TODOLIST
                        , new JsonArray().add(todolist.getId()).add(todolist.getTitle()).add(todolist.getStatus())
                        , updateWithParams -> {
                            connection.close();
                            if (updateWithParams.failed()) {
                                handler.handle(Future.failedFuture(updateWithParams.cause()));
                            } else {
                                handler.handle(Future.succeededFuture(updateWithParams.result().getUpdated() == 1));
                            }
                        });
            }
        });

    }

    @Override
    public void removeTodo(String todoId, Handler<AsyncResult<Boolean>> handler) {

        jdbcClient.getConnection(getConnection -> {
            if (getConnection.failed()) {
                handler.handle(Future.failedFuture(getConnection.cause()));
            } else {
                SQLConnection connection = getConnection.result();
                connection.updateWithParams(Constants.SQL_REMOVE_TODOLIST
                        , new JsonArray().add(todoId)
                        , updateWithParams -> {
                            connection.close();
                            if (updateWithParams.succeeded()) {
                                handler.handle(Future.succeededFuture(updateWithParams.result().getUpdated() == 1));
                            } else {
                                handler.handle(Future.failedFuture(updateWithParams.cause()));
                            }
                        });
            }
        });
    }

    @Override
    public void updateTodo(String todoId, String status, Handler<AsyncResult<Boolean>> handler) {

        jdbcClient.getConnection(getConnection -> {
            if (getConnection.failed()) {
                handler.handle(Future.failedFuture(getConnection.cause()));
            } else {
                SQLConnection connection = getConnection.result();
                connection.updateWithParams(Constants.SQL_UPDATE_TODOLIST
                        , new JsonArray().add(status).add(todoId)
                        , update -> {
                            connection.close();
                            if (update.succeeded()) {
                                handler.handle(Future.succeededFuture(update.result().getUpdated() == 1));
                            } else {
                                handler.handle(Future.failedFuture(update.cause()));
                            }
                        });
            }
        });
    }
}
