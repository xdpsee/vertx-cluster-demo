package pri.zhenhui.demo.uac;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.types.EventBusService;
import org.apache.commons.collections.CollectionUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import pri.zhenhui.demo.uac.domain.Role;
import pri.zhenhui.demo.uac.domain.enums.AuthorityType;
import pri.zhenhui.demo.uac.domain.enums.RoleType;
import pri.zhenhui.demo.uac.service.AuthorityService;
import pri.zhenhui.demo.uac.utils.DBUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertNotNull;
import static pri.zhenhui.demo.uac.MainVerticle.ROLE_AUTHORITIES;

@RunWith(VertxUnitRunner.class)
public class AuthorityServiceTests {

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
    public void testQueryRoles(TestContext context) {

        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(AuthorityService.SERVICE_NAME, AuthorityService.SERVICE_ADDRESS, AuthorityService.class));
        try {
            AuthorityService authorityService = reference.getAs(AuthorityService.class);
            assertNotNull(authorityService);

            final Async async = context.async();
            authorityService.queryRoles(queryRoles -> {
                try {
                    context.assertTrue(queryRoles.succeeded()
                            && queryRoles.result().size() == RoleType.values().length);
                } finally {
                    async.complete();
                }
            });

        } finally {
            reference.release();
        }

    }

    @Test
    public void testQueryAuthorities(TestContext context) {

        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(AuthorityService.SERVICE_NAME, AuthorityService.SERVICE_ADDRESS, AuthorityService.class));
        try {
            AuthorityService authorityService = reference.getAs(AuthorityService.class);
            assertNotNull(authorityService);

            final Async async = context.async();
            authorityService.queryAuthorities(queryAuthorities -> {
                try {
                    context.assertTrue(queryAuthorities.succeeded()
                            && queryAuthorities.result().size() == AuthorityType.values().length);
                } finally {
                    async.complete();
                }
            });

        } finally {
            reference.release();
        }
    }

    @Test
    public void testCreateUserRoles(TestContext context) {

        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(AuthorityService.SERVICE_NAME, AuthorityService.SERVICE_ADDRESS, AuthorityService.class));
        try {
            AuthorityService authorityService = reference.getAs(AuthorityService.class);
            assertNotNull(authorityService);

            final Async async = context.async();

            List<Role> roles = new ArrayList<>();
            roles.add(Role.from(RoleType.ADMIN));

            authorityService.createUserRoles(2L, roles, createUserRoles -> {
                context.assertTrue(createUserRoles.succeeded());

                authorityService.queryUserRoles(2L, queryUserRoles -> {
                    try {
                        context.assertTrue(queryUserRoles.succeeded() && !queryUserRoles.result().isEmpty());
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
    public void testQueryUserAuthorities(TestContext context) {

        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(AuthorityService.SERVICE_NAME, AuthorityService.SERVICE_ADDRESS, AuthorityService.class));
        try {
            AuthorityService authorityService = reference.getAs(AuthorityService.class);
            assertNotNull(authorityService);

            final Async async = context.async();

            List<Role> roles = new ArrayList<>();
            roles.add(Role.from(RoleType.ADMIN));

            authorityService.createUserRoles(2L, roles, createUserRoles -> {
                context.assertTrue(createUserRoles.succeeded());

                authorityService.queryUserAuthorities(2L, queryUserAuthorities -> {
                    context.assertTrue(queryUserAuthorities.succeeded()
                            && CollectionUtils.isNotEmpty(queryUserAuthorities.result()));

                    authorityService.deleteUserRoles(2L, roles, deleteUserRoles -> {
                        context.assertTrue(deleteUserRoles.succeeded());

                        authorityService.queryUserAuthorities(2L, queryUserAuthorities2 -> {

                            try {
                                context.assertTrue(queryUserAuthorities2.succeeded());
                                context.assertTrue(new HashSet<>(ROLE_AUTHORITIES.get(RoleType.USER))
                                        .containsAll(new HashSet<>(queryUserAuthorities2.result()
                                                .stream()
                                                .map(e -> AuthorityType.valueOf(e.getId()))
                                                .collect(Collectors.toList()))
                                        )
                                );
                            } finally {
                                async.complete();
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
