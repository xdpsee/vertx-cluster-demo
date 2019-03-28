package pri.zhenhui.demo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import pri.zhenhui.demo.todolist.TodolistDataServiceVerticle;
import pri.zhenhui.demo.todolist.TodolistRestfulVerticle;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) throws Exception {

        final JsonObject config = vertx.fileSystem().readFileBlocking("conf/http.config.json").toJsonObject();

        createDataService()
                .compose(ar -> createHttpServer(config))
                .setHandler(ar -> {
                    if (ar.failed()) {
                        ar.cause().printStackTrace();
                        startFuture.fail(ar.cause());
                    } else {
                        startFuture.complete();
                    }
                });

    }

    private Future<Void> createDataService() {

        Future future = Future.future();

        vertx.deployVerticle(TodolistDataServiceVerticle.class, new DeploymentOptions(), ar -> {
            if (ar.failed()) {
                future.fail(ar.cause());
            } else {
                future.complete();
            }
        });

        return future;
    }

    private Future<Void> createHttpServer(JsonObject config) {

        Future future = Future.future();

        vertx.deployVerticle(TodolistRestfulVerticle.class, new DeploymentOptions().setConfig(config), ar -> {
            if (ar.failed()) {
                future.fail(ar.cause());
            } else {
                future.complete();
            }
        });

        return future;
    }
}

