package pri.zhenhui.demo.udms;

import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.types.EventBusService;
import io.vertx.serviceproxy.ServiceException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import pri.zhenhui.demo.udms.domain.User;
import pri.zhenhui.demo.udms.manager.AccountManager;
import pri.zhenhui.demo.udms.service.UserTokenService;
import pri.zhenhui.demo.udms.service.exception.Errors;
import pri.zhenhui.demo.udms.utils.DBUtils;

@ExtendWith(VertxExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserTokenServiceTests {

    private ServiceDiscovery serviceDiscovery;

    @BeforeAll
    public void setup(Vertx vertx, VertxTestContext context) {

        serviceDiscovery = ServiceDiscovery.create(vertx);

        DBUtils.clearDB(vertx);

        vertx.deployVerticle(new MainVerticle(), context.completing());
    }

    @Test
    public void testGenerateToken(VertxTestContext context) {

        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(UserTokenService.SERVICE_NAME, UserTokenService.SERVICE_ADDRESS, UserTokenService.class));
        try {

            UserTokenService userTokenService = reference.getAs(UserTokenService.class);

            User user = new User();
            user.setUsername("ling100");
            user.setPhone("13402000080");
            user.setPassword(BCrypt.hashpw("12345678", BCrypt.gensalt()));

            AccountManager.getInstance().createUser(user).subscribe(userId -> {
                userTokenService.generateToken("ling100", "12345678", generateToken -> {
                    if (generateToken.failed()) {
                        context.failNow(generateToken.cause());
                    } else {
                        context.completeNow();
                    }
                });
            }, error -> {
                context.failNow(error);
            });
        } finally {
            reference.release();
        }
    }

    @Test
    public void testGenerateTokenAbsentUser(VertxTestContext context) {

        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(UserTokenService.SERVICE_NAME, UserTokenService.SERVICE_ADDRESS, UserTokenService.class));
        try {
            UserTokenService userTokenService = reference.getAs(UserTokenService.class);
            userTokenService.generateToken("test_absent", "12345678", generateToken -> {
                if (generateToken.succeeded()) {
                    context.failNow(new AssertionError(""));
                } else {
                    ServiceException exception = (ServiceException) generateToken.cause();
                    if (exception.failureCode() == Errors.USER_NOT_FOUND.code) {
                        context.completeNow();
                    } else {
                        context.failNow(generateToken.cause());
                    }
                }
            });
        } finally {
            reference.release();
        }
    }

    @Test
    public void testGenerateTokenPasswordMismatch(VertxTestContext context) {

        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(UserTokenService.SERVICE_NAME, UserTokenService.SERVICE_ADDRESS, UserTokenService.class));
        try {

            UserTokenService userTokenService = reference.getAs(UserTokenService.class);

            User user = new User();
            user.setUsername("ling101");
            user.setPhone("13402001080");
            user.setPassword(BCrypt.hashpw("12345678", BCrypt.gensalt()));

            AccountManager.getInstance().createUser(user).subscribe(userId -> {
                userTokenService.generateToken("ling101", "78961233", generateToken -> {
                    ServiceException exception = (ServiceException) generateToken.cause();
                    if (exception.failureCode() == Errors.USER_PASSWORD_MISMATCH.code) {
                        context.completeNow();
                    } else {
                        context.failNow(generateToken.cause());
                    }
                });
            }, error -> {
                context.failNow(error);
            });
        } finally {
            reference.release();
        }
    }
}
