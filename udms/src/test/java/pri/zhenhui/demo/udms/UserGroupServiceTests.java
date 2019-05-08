package pri.zhenhui.demo.udms;

import io.vertx.core.Vertx;
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
import pri.zhenhui.demo.udms.domain.Permission;
import pri.zhenhui.demo.udms.domain.User;
import pri.zhenhui.demo.udms.service.AccountService;
import pri.zhenhui.demo.udms.service.UserGroupService;
import pri.zhenhui.demo.udms.utils.DBUtils;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(VertxExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserGroupServiceTests {

    private ServiceDiscovery serviceDiscovery;

    @BeforeAll
    public void setup(Vertx vertx, VertxTestContext context) {

        serviceDiscovery = ServiceDiscovery.create(vertx);

        DBUtils.clearDB(vertx);

        vertx.deployVerticle(new MainVerticle(), context.completing());
    }


    @Test
    public void testCreateUserGroup(VertxTestContext context) {

        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(UserGroupService.SERVICE_NAME, UserGroupService.SERVICE_ADDRESS, UserGroupService.class));
        try {

            UserGroupService userGroupService = reference.getAs(UserGroupService.class);

            userGroupService.createGroup(1L, "测试分组1", createGroup -> {
                if (createGroup.failed()) {
                    context.failNow(createGroup.cause());
                    return;
                }

                context.completeNow();
            });

        } finally {
            reference.release();
        }


    }

    @Test
    public void testRemoveUserGroup(VertxTestContext context) {

        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(UserGroupService.SERVICE_NAME, UserGroupService.SERVICE_ADDRESS, UserGroupService.class));
        try {

            UserGroupService userGroupService = reference.getAs(UserGroupService.class);

            userGroupService.createGroup(2L, "测试分组2", createGroup -> {
                if (createGroup.failed()) {
                    context.failNow(createGroup.cause());
                    return;
                }

                userGroupService.removeGroup(2L, createGroup.result(), removeGroup -> {
                    if (createGroup.failed()) {
                        context.failNow(createGroup.cause());
                        return;
                    }

                    try {
                        assertTrue(removeGroup.result());
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
    public void testAddUserToGroup(VertxTestContext context) {

        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(UserGroupService.SERVICE_NAME, UserGroupService.SERVICE_ADDRESS, UserGroupService.class));
        try {

            UserGroupService userGroupService = reference.getAs(UserGroupService.class);

            userGroupService.createGroup(3L, "测试分组3", createGroup -> {
                if (createGroup.failed()) {
                    context.failNow(createGroup.cause());
                    return;
                }

                userGroupService.addUserToGroup(createGroup.result(), 3L, addUserToGroup -> {
                    if (addUserToGroup.failed()) {
                        context.failNow(addUserToGroup.cause());
                        return;
                    }

                    try {
                        assertTrue(addUserToGroup.result());
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
    public void testRemoveUserFromGroup(VertxTestContext context) {

        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(UserGroupService.SERVICE_NAME, UserGroupService.SERVICE_ADDRESS, UserGroupService.class));
        try {

            UserGroupService userGroupService = reference.getAs(UserGroupService.class);

            userGroupService.createGroup(4L, "测试分组4", createGroup -> {
                if (createGroup.failed()) {
                    context.failNow(createGroup.cause());
                    return;
                }

                userGroupService.addUserToGroup(createGroup.result(), 4L, addUserToGroup -> {
                    if (addUserToGroup.failed()) {
                        context.failNow(addUserToGroup.cause());
                        return;
                    }

                    userGroupService.removeUserFromGroup(createGroup.result(), 4L, removeUserFromGroup -> {
                        if (removeUserFromGroup.failed()) {
                            context.failNow(removeUserFromGroup.cause());
                            return;
                        }

                        try {
                            assertTrue(removeUserFromGroup.result());
                            context.completeNow();
                        } catch (Throwable e) {
                            context.failNow(e);
                        }

                    });

                });
            });

        } finally {
            reference.release();
        }
    }

    @Test
    public void testAssignGroupPermission(VertxTestContext context) {

        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(UserGroupService.SERVICE_NAME, UserGroupService.SERVICE_ADDRESS, UserGroupService.class));
        try {

            UserGroupService userGroupService = reference.getAs(UserGroupService.class);

            userGroupService.assignGroupPermission(1L, 1L, assignGroupPermission -> {
                if (assignGroupPermission.failed()) {
                    context.failNow(assignGroupPermission.cause());
                } else {
                    context.completeNow();
                }
            });

        } finally {
            reference.release();
        }

    }

    @Test
    public void testDeassignGroupPermission(VertxTestContext context) {

        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(UserGroupService.SERVICE_NAME, UserGroupService.SERVICE_ADDRESS, UserGroupService.class));
        try {

            UserGroupService userGroupService = reference.getAs(UserGroupService.class);

            userGroupService.assignGroupPermission(2L, 1L, assignGroupPermission -> {
                if (assignGroupPermission.failed()) {
                    context.failNow(assignGroupPermission.cause());
                } else {
                    userGroupService.deassignGroupPermission(2L, 1L, deassignGroupPermission -> {
                        if (deassignGroupPermission.failed()) {
                            context.failNow(deassignGroupPermission.cause());
                        } else {
                            context.completeNow();
                        }
                    });
                }
            });

        } finally {
            reference.release();
        }

    }

    @Test
    public void testQueryGroupPermissions(VertxTestContext context) {

        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(UserGroupService.SERVICE_NAME, UserGroupService.SERVICE_ADDRESS, UserGroupService.class));
        try {

            UserGroupService userGroupService = reference.getAs(UserGroupService.class);

            userGroupService.assignGroupPermission(2L, 1L, assignGroupPermission -> {
                if (assignGroupPermission.failed()) {
                    context.failNow(assignGroupPermission.cause());
                } else {
                    userGroupService.queryGroupPermissions(2L, queryGroupPermissions -> {
                        StackTrace.printIfErr(queryGroupPermissions);
                        if (queryGroupPermissions.failed()) {
                            context.failNow(queryGroupPermissions.cause());
                        } else {
                            try {
                                assertTrue(queryGroupPermissions.result().contains(Permission.valueOf(1L).name()));
                                context.completeNow();
                            } catch (Throwable e) {
                                context.failNow(e);
                            }
                        }
                    });
                }
            });
        } finally {
            reference.release();
        }
    }

    @Test
    public void testQueryGroups(VertxTestContext context) {

        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(UserGroupService.SERVICE_NAME, UserGroupService.SERVICE_ADDRESS, UserGroupService.class));
        try {

            UserGroupService userGroupService = reference.getAs(UserGroupService.class);

            userGroupService.createGroup(6L, "测试分组6", createGroup -> {
                if (createGroup.succeeded()) {
                    userGroupService.queryGroups(6L, queryGroups -> {
                        if (queryGroups.failed()) {
                            context.failNow(queryGroups.cause());
                        } else {
                            try {
                                assertTrue(queryGroups.result().stream().anyMatch(e -> e.getTitle().equals("测试分组6")));
                                context.completeNow();
                            } catch (Throwable e) {
                                context.failNow(e);
                            }
                        }
                    });

                    return;
                } else {
                    context.failNow(createGroup.cause());
                }
            });

        } finally {
            reference.release();
        }
    }

    @Test
    public void testQueryGroupMembers(VertxTestContext context) {

        ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(UserGroupService.SERVICE_NAME, UserGroupService.SERVICE_ADDRESS, UserGroupService.class));
        try {
            UserGroupService userGroupService = reference.getAs(UserGroupService.class);

            userGroupService.createGroup(7L, "测试分组7", createGroup -> {
                if (createGroup.failed()) {
                    context.failNow(createGroup.cause());
                    return;
                }

                ServiceReference ref = serviceDiscovery.getReference(EventBusService.createRecord(AccountService.SERVICE_NAME, AccountService.SERVICE_ADDRESS, AccountService.class));
                try {
                    AccountService accountService = ref.getAs(AccountService.class);
                    User user = new User();
                    user.setUsername("zhenhui84");
                    user.setPhone("13402021984");
                    user.setPassword(BCrypt.hashpw("12345678", BCrypt.gensalt()));
                    accountService.createUser(user, createUser -> {
                        if (createUser.failed()) {
                            context.failNow(createUser.cause());
                            return;
                        }

                        userGroupService.addUserToGroup(createGroup.result(), createUser.result(), addUserToGroup -> {
                            userGroupService.queryGroupMembers(createGroup.result(), queryGroupMembers -> {
                                try {
                                    assertTrue(queryGroupMembers.result().stream().anyMatch(e -> e.getId().equals(createUser.result())));
                                    context.completeNow();
                                } catch (Throwable e) {
                                    context.failNow(e);
                                }
                            });
                        });
                    });
                } finally {
                    ref.release();
                }
            });

        } finally {
            reference.release();
        }



    }
}
