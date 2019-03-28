package pri.zhenhui.demo.todolist;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.serviceproxy.ServiceBinder;

public class TodolistDataServiceVerticle extends AbstractVerticle {

    private ServiceBinder serviceBinder;

    private MessageConsumer<JsonObject> registry;

    private ServiceDiscovery discovery;

    private JDBCClient jdbcClient;

    @Override
    public void start(Future<Void> startFuture) throws Exception {

        initDatabase().setHandler(ar -> {
            if (ar.failed()) {
                startFuture.fail(ar.cause());
            } else {
                TodolistDataService todolistDataService = new TodolistDataServiceImpl(jdbcClient);

                serviceBinder = new ServiceBinder(vertx);
                registry = serviceBinder.setAddress(TodolistDataService.SERVICE_ADDRESS)
                        .register(TodolistDataService.class, todolistDataService);


                discovery = ServiceDiscovery.create(vertx, r ->
                        r.publish(TodolistDataService.RECORD, arp -> {
                            if (arp.failed()) {
                                startFuture.fail(arp.cause());
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

        if (discovery != null) {
            discovery.unpublish(TodolistDataService.RECORD.getRegistration(), ar -> {
                if (ar.failed()) {
                    stopFuture.fail(ar.cause());
                } else {
                    stopFuture.complete();
                }

                discovery.close();
            });
        } else {
            stopFuture.complete();
        }
    }

    private Future<Void> initDatabase() {

        final Future<Void> future = Future.future();

        vertx.fileSystem().readFile("conf/jdbc.config.json", arb -> {
            if (arb.failed()) {
                future.fail(arb.cause());
            } else {
                jdbcClient = JDBCClient.createShared(vertx, arb.result().toJsonObject());
                jdbcClient.getConnection(arc -> {
                    if (arc.failed()) {
                        arc.cause().printStackTrace();
                        future.fail(arc.cause());
                    } else {
                        SQLConnection connection = arc.result();
                        connection.execute(Constants.SQL_CREATE_TODOLIST_TABLE, ar -> {
                            connection.close();

                            if (ar.failed()) {
                                ar.cause().printStackTrace();
                                future.fail(ar.cause());
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




