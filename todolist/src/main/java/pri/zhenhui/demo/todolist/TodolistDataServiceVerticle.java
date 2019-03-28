package pri.zhenhui.demo.todolist;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.types.EventBusService;
import io.vertx.serviceproxy.ServiceBinder;

public class TodolistDataServiceVerticle extends AbstractVerticle {

    private ServiceBinder serviceBinder;

    private MessageConsumer<JsonObject> registry;

    private ServiceDiscovery serviceDiscovery;

    private Record serviceRecord = EventBusService.createRecord(TodolistDataService.SERVICE_NAME, TodolistDataService.SERVICE_ADDRESS, TodolistDataService.class);

    private JDBCClient jdbcClient;

    @Override
    public void start(Future<Void> startFuture) throws Exception {

        initDatabase().setHandler(initDatabase -> {
            if (initDatabase.failed()) {
                startFuture.fail(initDatabase.cause());
            } else {
                TodolistDataService todolistDataService = new TodolistDataServiceImpl(jdbcClient);

                serviceBinder = new ServiceBinder(vertx);
                registry = serviceBinder.setAddress(TodolistDataService.SERVICE_ADDRESS)
                        .register(TodolistDataService.class, todolistDataService);


                serviceDiscovery = ServiceDiscovery.create(vertx, r ->
                        r.publish(serviceRecord, publish -> {
                            if (publish.failed()) {
                                startFuture.fail(publish.cause());
                            } else {
                                startFuture.complete();
                            }
                        })
                );
            }
        });
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {

        if (registry != null && registry.isRegistered()) {
            serviceBinder.unregister(registry);
        }

        if (serviceDiscovery != null) {
            serviceDiscovery.unpublish(serviceRecord.getRegistration(), ar -> {
                if (ar.failed()) {
                    stopFuture.fail(ar.cause());
                } else {
                    stopFuture.complete();
                }

                serviceDiscovery.close();
            });
        } else {
            stopFuture.complete();
        }
    }

    private Future<Void> initDatabase() {

        final Future<Void> future = Future.future();

        vertx.fileSystem().readFile("conf/jdbc.config.json", readFile -> {
            if (readFile.failed()) {
                future.fail(readFile.cause());
            } else {
                jdbcClient = JDBCClient.createShared(vertx, readFile.result().toJsonObject());
                jdbcClient.getConnection(getConnection -> {
                    if (getConnection.failed()) {
                        future.fail(getConnection.cause());
                    } else {
                        SQLConnection connection = getConnection.result();
                        connection.execute(Constants.SQL_CREATE_TODOLIST_TABLE, execute -> {
                            connection.close();
                            if (execute.failed()) {
                                future.fail(execute.cause());
                            } else {
                                future.complete();
                            }
                        });
                    }
                });
            }
        });

        return future;
    }

}




