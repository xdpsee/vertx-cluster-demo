package pri.zhenhui.demo.udms;

import com.google.common.collect.Sets;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.types.EventBusService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import pri.zhenhui.demo.support.test.StackTrace;
import pri.zhenhui.demo.udms.domain.User;
import pri.zhenhui.demo.udms.service.AccountService;
import pri.zhenhui.demo.udms.utils.DBUtils;

import static org.jgroups.util.Util.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(VertxExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AccountServiceTests {

    private ServiceDiscovery serviceDiscovery;

    @BeforeAll
    public void setup(Vertx vertx, VertxTestContext context) {

        serviceDiscovery = ServiceDiscovery.create(vertx);

        DBUtils.clearDB(vertx);

        vertx.deployVerticle(new MainVerticle(), context.completing());
    }

    @Test
    public void testCreateUser(VertxTestContext context) {
        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(AccountService.SERVICE_NAME, AccountService.SERVICE_ADDRESS, AccountService.class));
        try {
            AccountService accountService = reference.getAs(AccountService.class);
            assertNotNull(accountService);

            User user = new User();
            user.setUsername("ling");
            user.setPhone("13402022080");
            user.setPassword(BCrypt.hashpw("12345678", BCrypt.gensalt()));


            accountService.createUser(user, create -> {
                try {
                    if (create.succeeded()) {
                        context.completeNow();
                    } else {
                        StackTrace.printIfErr(create);
                        context.failNow(create.cause());
                    }
                } catch (Throwable e) {
                    context.failNow(e);
                }
            });

        } finally {
            reference.release();
        }

    }

    @Test
    public void testQueryUserById(VertxTestContext context) {

        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(AccountService.SERVICE_NAME, AccountService.SERVICE_ADDRESS, AccountService.class));
        try {
            AccountService accountService = reference.getAs(AccountService.class);
            assertNotNull(accountService);

            User user = new User();
            user.setUsername("ling1");
            user.setPhone("13402022081");
            user.setPassword(BCrypt.hashpw("12345678", BCrypt.gensalt()));


            accountService.createUser(user, create -> {
                if (!create.succeeded()) {
                    context.failNow(create.cause());
                } else {
                    accountService.queryUserById(create.result(), queryUserById -> {
                        StackTrace.printIfErr(queryUserById);
                        try {
                            assertTrue(queryUserById.succeeded() && queryUserById.result() != null);
                            context.completeNow();
                        } catch (Throwable e) {
                            context.failNow(e);
                        }
                    });
                }
            });

        } finally {
            reference.release();
        }
    }

    @Test
    public void testQueryUserByIds(VertxTestContext context) {

        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(AccountService.SERVICE_NAME, AccountService.SERVICE_ADDRESS, AccountService.class));
        try {
            AccountService accountService = reference.getAs(AccountService.class);
            assertNotNull(accountService);

            User user = new User();
            user.setUsername("ling300");
            user.setPhone("13402022000");
            user.setPassword(BCrypt.hashpw("12345678", BCrypt.gensalt()));


            accountService.createUser(user, create -> {
                if (!create.succeeded()) {
                    context.failNow(create.cause());
                } else {
                    accountService.queryUserByIds(Sets.newHashSet(create.result()), queryUserByIds -> {
                        StackTrace.printIfErr(queryUserByIds);
                        try {
                            assertTrue(queryUserByIds.succeeded() && !queryUserByIds.result().isEmpty());
                            context.completeNow();
                        } catch (Throwable e) {
                            context.failNow(e);
                        }
                    });
                }
            });

        } finally {
            reference.release();
        }
    }

    @Test
    public void testQueryUserByName(VertxTestContext context) {

        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(AccountService.SERVICE_NAME, AccountService.SERVICE_ADDRESS, AccountService.class));
        try {
            AccountService accountService = reference.getAs(AccountService.class);
            assertNotNull(accountService);

            User user = new User();
            user.setUsername("ling2");
            user.setPhone("13402022082");
            user.setPassword(BCrypt.hashpw("12345678", BCrypt.gensalt()));


            accountService.createUser(user, create -> {
                if (!create.succeeded()) {
                    context.failNow(create.cause());
                }

                if (create.failed()) {
                    context.failNow(create.cause());
                } else {
                    accountService.queryUserByName("ling2", queryUserByName -> {
                        try {
                            assertTrue(queryUserByName.succeeded() && queryUserByName.result() != null);
                            context.completeNow();
                        } catch (Throwable e) {
                            context.failNow(e);
                        }
                    });
                }
            });

        } finally {
            reference.release();
        }
    }

    @Test
    public void testQueryUserByPhone(VertxTestContext context) {

        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(AccountService.SERVICE_NAME, AccountService.SERVICE_ADDRESS, AccountService.class));
        try {
            AccountService accountService = reference.getAs(AccountService.class);
            assertNotNull(accountService);

            User user = new User();
            user.setUsername("ling3");
            user.setPhone("13402022083");
            user.setPassword(BCrypt.hashpw("12345678", BCrypt.gensalt()));

            accountService.createUser(user, create -> {
                if (create.failed()) {
                    context.failNow(create.cause());
                } else {
                    accountService.queryUserByPhone("13402022083", queryUserByPhone -> {
                        try {
                            assertTrue(queryUserByPhone.succeeded() && queryUserByPhone.result() != null);
                            context.completeNow();
                        } catch (Throwable e) {
                            context.failNow(e);
                        }
                    });
                }
            });
        } finally {
            reference.release();
        }
    }

    @Test
    public void testUpdateUser(VertxTestContext context) {

        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(AccountService.SERVICE_NAME, AccountService.SERVICE_ADDRESS, AccountService.class));
        try {
            AccountService accountService = reference.getAs(AccountService.class);
            assertNotNull(accountService);

            User user = new User();
            user.setUsername("ling4");
            user.setPhone("13402022084");
            user.setPassword(BCrypt.hashpw("12345678", BCrypt.gensalt()));


            accountService.createUser(user, create -> {
                if (create.failed()) {
                    context.failNow(create.cause());
                    return;
                }

                JsonObject params = new JsonObject()
                        .put("id", create.result())
                        .put("email", "lat@vv.com")
                        .put("avatar", "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2075158174,1947607517&fm=26&gp=0.jpg");

                accountService.updateUser(params, updateUser -> {
                    try {
                        assertTrue(updateUser.succeeded() && updateUser.result());
                        context.completeNow();
                    } catch (Throwable e) {
                        context.failNow(e);
                    }
                });

            });
        } finally {
            reference.release();
        }
    }

    @Test
    public void testQueryByParent(VertxTestContext context) {
        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(AccountService.SERVICE_NAME, AccountService.SERVICE_ADDRESS, AccountService.class));
        try {
            AccountService accountService = reference.getAs(AccountService.class);
            assertNotNull(accountService);

            User user = new User();
            user.setParentId(1L);
            user.setUsername("ling5");
            user.setPhone("13402022085");
            user.setPassword(BCrypt.hashpw("12345678", BCrypt.gensalt()));

            accountService.createUser(user, create -> {
                if (create.failed()) {
                    context.failNow(create.cause());
                    return;
                }
                accountService.queryUsers(1L, queryUsers -> {
                    try {
                        assertTrue(queryUsers.succeeded() && queryUsers.result().size() == 1);
                        context.completeNow();
                    } catch (Throwable e) {
                        context.failNow(e);
                    }
                });

            });
        } finally {
            reference.release();
        }
    }

}
