package pri.zhenhui.demo.udms;

import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.types.EventBusService;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import pri.zhenhui.demo.udms.domain.Role;
import pri.zhenhui.demo.udms.domain.enums.AuthorityType;
import pri.zhenhui.demo.udms.domain.enums.RoleType;
import pri.zhenhui.demo.udms.service.AuthorityService;
import pri.zhenhui.demo.udms.utils.DBUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pri.zhenhui.demo.udms.MainVerticle.ROLE_AUTHORITIES;

@ExtendWith(VertxExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthorityServiceTests {

    private ServiceDiscovery serviceDiscovery;

    @BeforeAll
    public void setup(Vertx vertx, VertxTestContext context) {

        serviceDiscovery = ServiceDiscovery.create(vertx);

        DBUtils.clearDB(vertx);

        vertx.deployVerticle(new MainVerticle(), context.completing());
    }

    @Test
    public void testQueryRoles(VertxTestContext context) {

        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(AuthorityService.SERVICE_NAME, AuthorityService.SERVICE_ADDRESS, AuthorityService.class));
        try {
            AuthorityService authorityService = reference.getAs(AuthorityService.class);
            assertNotNull(authorityService);

            authorityService.queryRoles(queryRoles -> {
                try {
                    assertTrue(queryRoles.succeeded()
                            && queryRoles.result().size() == RoleType.values().length);
                    context.completeNow();
                } catch (Throwable e) {
                    context.failNow(e);
                }
            });

        } finally {
            reference.release();
        }

    }

    @Test
    public void testQueryAuthorities(VertxTestContext context) {

        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(AuthorityService.SERVICE_NAME, AuthorityService.SERVICE_ADDRESS, AuthorityService.class));
        try {
            AuthorityService authorityService = reference.getAs(AuthorityService.class);
            assertNotNull(authorityService);

            authorityService.queryAuthorities(queryAuthorities -> {
                try {
                    assertTrue(queryAuthorities.succeeded()
                            && queryAuthorities.result().size() == AuthorityType.values().length);
                    context.completeNow();
                } catch (Throwable e) {
                    context.failNow(e);
                }
            });

        } finally {
            reference.release();
        }
    }

    @Test
    public void testCreateUserRoles(VertxTestContext context) {

        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(AuthorityService.SERVICE_NAME, AuthorityService.SERVICE_ADDRESS, AuthorityService.class));
        try {
            AuthorityService authorityService = reference.getAs(AuthorityService.class);
            assertNotNull(authorityService);

            List<Role> roles = new ArrayList<>();
            roles.add(Role.from(RoleType.ADMIN));

            authorityService.createUserRoles(2L, roles, createUserRoles -> {
                assertTrue(createUserRoles.succeeded());

                authorityService.queryUserRoles(2L, queryUserRoles -> {
                    try {
                        assertTrue(queryUserRoles.succeeded() && !queryUserRoles.result().isEmpty());
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
    public void testQueryUserAuthorities(VertxTestContext context) {

        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(AuthorityService.SERVICE_NAME, AuthorityService.SERVICE_ADDRESS, AuthorityService.class));
        try {
            AuthorityService authorityService = reference.getAs(AuthorityService.class);
            assertNotNull(authorityService);

            List<Role> roles = new ArrayList<>();
            roles.add(Role.from(RoleType.ADMIN));

            authorityService.createUserRoles(2L, roles, createUserRoles -> {
                if (createUserRoles.failed()) {
                    context.failNow(createUserRoles.cause());
                    return;
                }

                authorityService.queryUserAuthorities(2L, queryUserAuthorities -> {
                    try {
                        assertTrue(queryUserAuthorities.succeeded()
                                && CollectionUtils.isNotEmpty(queryUserAuthorities.result()));
                    } catch (Exception e) {
                        context.failNow(e);
                        return;
                    }

                    authorityService.deleteUserRoles(2L, roles, deleteUserRoles -> {
                        if (deleteUserRoles.failed()) {
                            context.failNow(deleteUserRoles.cause());
                            return;
                        }

                        authorityService.queryUserAuthorities(2L, queryUserAuthorities2 -> {

                            try {
                                assertTrue(queryUserAuthorities2.succeeded());
                                assertTrue(new HashSet<>(ROLE_AUTHORITIES.get(RoleType.USER))
                                        .containsAll(new HashSet<>(queryUserAuthorities2.result()
                                                .stream()
                                                .map(e -> AuthorityType.valueOf(e.getId()))
                                                .collect(Collectors.toList()))
                                        )
                                );
                                context.completeNow();
                            } catch (Throwable e){
                                context.failNow(e);
                            }
                        });
                    });
                });
            });

        } finally {
            reference.release();
        }
    }

}
