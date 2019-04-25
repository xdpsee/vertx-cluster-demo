package pri.zhenhui.demo.uac;

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
import pri.zhenhui.demo.uac.domain.User;
import pri.zhenhui.demo.uac.service.AccountService;
import pri.zhenhui.demo.uac.utils.DBUtils;

import java.util.Date;

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
            user.setCreateAt(new Date());
            user.setPassword(BCrypt.hashpw("12345678", BCrypt.gensalt()));


            accountService.createUser(user, create -> {
                try {
                    StackTrace.printIfErr(create);
                    assertTrue(create.succeeded());
                } finally {
                    context.completeNow();
                }
            });

        } finally {
            reference.release();
        }

    }

    @Test
    public void testSelectUserById(VertxTestContext context) {

        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(AccountService.SERVICE_NAME, AccountService.SERVICE_ADDRESS, AccountService.class));
        try {
            AccountService accountService = reference.getAs(AccountService.class);
            assertNotNull(accountService);

            User user = new User();
            user.setUsername("ling1");
            user.setPhone("13402022081");
            user.setCreateAt(new Date());
            user.setPassword(BCrypt.hashpw("12345678", BCrypt.gensalt()));


            accountService.createUser(user, create -> {
                assertTrue(create.succeeded());
                accountService.queryUserById(create.result(), queryUserById -> {
                    StackTrace.printIfErr(queryUserById);
                    try {
                        assertTrue(queryUserById.succeeded());
                    } finally {
                        context.completeNow();
                    }
                });
            });

        } finally {
            reference.release();
        }
    }

    @Test
    public void testSelectUserByName(VertxTestContext context) {

        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(AccountService.SERVICE_NAME, AccountService.SERVICE_ADDRESS, AccountService.class));
        try {
            AccountService accountService = reference.getAs(AccountService.class);
            assertNotNull(accountService);

            User user = new User();
            user.setUsername("ling2");
            user.setPhone("13402022082");
            user.setCreateAt(new Date());
            user.setPassword(BCrypt.hashpw("12345678", BCrypt.gensalt()));


            accountService.createUser(user, create -> {
                assertTrue(create.succeeded());
                accountService.queryUserByName("ling2", queryUserByName -> {
                    StackTrace.printIfErr(queryUserByName);
                    try {
                        assertTrue(queryUserByName.succeeded());
                    } finally {
                        context.completeNow();
                    }
                });
            });

        } finally {
            reference.release();
        }
    }

    @Test
    public void testSelectUserByPhone(VertxTestContext context) {

        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(AccountService.SERVICE_NAME, AccountService.SERVICE_ADDRESS, AccountService.class));
        try {
            AccountService accountService = reference.getAs(AccountService.class);
            assertNotNull(accountService);

            User user = new User();
            user.setUsername("ling3");
            user.setPhone("13402022083");
            user.setCreateAt(new Date());
            user.setPassword(BCrypt.hashpw("12345678", BCrypt.gensalt()));

            accountService.createUser(user, create -> {
                assertTrue(create.succeeded());
                accountService.queryUserByPhone("13402022083", queryUserByPhone -> {
                    StackTrace.printIfErr(queryUserByPhone);
                    try {
                        assertTrue(queryUserByPhone.succeeded());
                    } finally {
                        context.completeNow();
                    }
                });
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
            user.setCreateAt(new Date());
            user.setPassword(BCrypt.hashpw("12345678", BCrypt.gensalt()));


            accountService.createUser(user, create -> {
                assertTrue(create.succeeded());

                JsonObject params = new JsonObject()
                        .put("id", create.result())
                        .put("email", "lat@vv.com")
                        .put("avatar", "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2075158174,1947607517&fm=26&gp=0.jpg");

                accountService.updateUser(params, updateUser -> {
                    try {
                        assertTrue(updateUser.succeeded() && updateUser.result());

                    } finally {
                        context.completeNow();
                    }
                }) ;

            });
        } finally {
            reference.release();
        }
    }


}
