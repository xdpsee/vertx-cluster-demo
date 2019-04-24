package pri.zhenhui.demo.uac;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.types.EventBusService;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mindrot.jbcrypt.BCrypt;
import pri.zhenhui.demo.uac.domain.User;
import pri.zhenhui.demo.uac.service.AccountService;
import pri.zhenhui.demo.uac.utils.DBUtils;
import pri.zhenhui.demo.uac.utils.StackTrace;

import java.util.Date;

import static junit.framework.TestCase.assertNotNull;

@RunWith(VertxUnitRunner.class)
public class AccountServiceTests {

    private static Vertx vertx;

    private static ServiceDiscovery serviceDiscovery;

    @BeforeClass
    public static void setup(TestContext context) {
        Async async = context.async();

        vertx = Vertx.vertx(new VertxOptions());
        DBUtils.clearDB(vertx);

        vertx.deployVerticle(new MainVerticle(), deploy -> {
            serviceDiscovery = ServiceDiscovery.create(vertx);
            async.complete();
        });
    }

    @AfterClass
    public static void tearDown(TestContext context) {
        serviceDiscovery.close();
        vertx.close();
    }

    @Test
    public void testCreateUser(TestContext context) {
        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(AccountService.SERVICE_NAME, AccountService.SERVICE_ADDRESS, AccountService.class));
        try {
            AccountService accountService = reference.getAs(AccountService.class);
            assertNotNull(accountService);

            User user = new User();
            user.setUsername("ling");
            user.setPhone("13402022080");
            user.setCreateAt(new Date());
            user.setPassword(BCrypt.hashpw("12345678", BCrypt.gensalt()));

            final Async async = context.async();

            accountService.createUser(user, create -> {
                try {
                    StackTrace.print(create);
                    context.assertTrue(create.succeeded());
                } finally {
                    async.complete();
                }
            });

        } finally {
            reference.release();
        }

    }

    @Test
    public void testSelectUserById(TestContext context) {

        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(AccountService.SERVICE_NAME, AccountService.SERVICE_ADDRESS, AccountService.class));
        try {
            AccountService accountService = reference.getAs(AccountService.class);
            assertNotNull(accountService);

            User user = new User();
            user.setUsername("ling1");
            user.setPhone("13402022081");
            user.setCreateAt(new Date());
            user.setPassword(BCrypt.hashpw("12345678", BCrypt.gensalt()));

            final Async async = context.async();

            accountService.createUser(user, create -> {
                context.assertTrue(create.succeeded());
                accountService.queryUserById(create.result(), queryUserById -> {
                    StackTrace.print(queryUserById);
                    try {
                        context.assertTrue(queryUserById.succeeded());
                    } finally {
                        async.complete();
                    }
                });
            });

        } finally {
            reference.release();
        }
    }

    @Test
    public void testSelectUserByName(TestContext context) {

        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(AccountService.SERVICE_NAME, AccountService.SERVICE_ADDRESS, AccountService.class));
        try {
            AccountService accountService = reference.getAs(AccountService.class);
            assertNotNull(accountService);

            User user = new User();
            user.setUsername("ling2");
            user.setPhone("13402022082");
            user.setCreateAt(new Date());
            user.setPassword(BCrypt.hashpw("12345678", BCrypt.gensalt()));

            final Async async = context.async();

            accountService.createUser(user, create -> {
                context.assertTrue(create.succeeded());
                accountService.queryUserByName("ling2", queryUserByName -> {
                    StackTrace.print(queryUserByName);
                    try {
                        context.assertTrue(queryUserByName.succeeded());
                    } finally {
                        async.complete();
                    }
                });
            });

        } finally {
            reference.release();
        }
    }

    @Test
    public void testSelectUserByPhone(TestContext context) {

        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(AccountService.SERVICE_NAME, AccountService.SERVICE_ADDRESS, AccountService.class));
        try {
            AccountService accountService = reference.getAs(AccountService.class);
            assertNotNull(accountService);

            User user = new User();
            user.setUsername("ling3");
            user.setPhone("13402022083");
            user.setCreateAt(new Date());
            user.setPassword(BCrypt.hashpw("12345678", BCrypt.gensalt()));

            final Async async = context.async();

            accountService.createUser(user, create -> {
                context.assertTrue(create.succeeded());
                accountService.queryUserByPhone("13402022083", queryUserByPhone -> {
                    StackTrace.print(queryUserByPhone);
                    try {
                        context.assertTrue(queryUserByPhone.succeeded());
                    } finally {
                        async.complete();
                    }
                });
            });
        } finally {
            reference.release();
        }
    }

    @Test
    public void testUpdateUser(TestContext context) {

        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(AccountService.SERVICE_NAME, AccountService.SERVICE_ADDRESS, AccountService.class));
        try {
            AccountService accountService = reference.getAs(AccountService.class);
            assertNotNull(accountService);

            User user = new User();
            user.setUsername("ling4");
            user.setPhone("13402022084");
            user.setCreateAt(new Date());
            user.setPassword(BCrypt.hashpw("12345678", BCrypt.gensalt()));

            final Async async = context.async();

            accountService.createUser(user, create -> {
                context.assertTrue(create.succeeded());

                JsonObject params = new JsonObject()
                        .put("id", create.result())
                        .put("email", "lat@vv.com")
                        .put("avatar", "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2075158174,1947607517&fm=26&gp=0.jpg");

                accountService.updateUser(params, updateUser -> {
                    try {
                        context.assertTrue(updateUser.succeeded() && updateUser.result());

                    } finally {
                        async.complete();
                    }
                }) ;

            });
        } finally {
            reference.release();
        }
    }


}
