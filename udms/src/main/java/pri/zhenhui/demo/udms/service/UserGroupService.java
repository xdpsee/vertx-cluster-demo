package pri.zhenhui.demo.udms.service;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import pri.zhenhui.demo.udms.domain.UserGroup;
import pri.zhenhui.demo.udms.domain.UserGroupMember;

import java.util.List;

@VertxGen
@ProxyGen
public interface UserGroupService {

    String SERVICE_NAME = "service.data.user.group";

    String SERVICE_ADDRESS = "address.service.data.user.group";

    void createGroup(Long userId, String title, Handler<AsyncResult<Long>> resultHandler);

    void removeGroup(Long userId, Long groupId,  Handler<AsyncResult<Boolean>> resultHandler);

    void assignGroupPermission(Long groupId, Long permission, Handler<AsyncResult<Boolean>> resultHandler);

    void deassignGroupPermission(Long groupId, Long permission, Handler<AsyncResult<Boolean>> resultHandler);

    void queryGroupPermissions(Long groupId, Handler<AsyncResult<List<String>>> resultHandler);

    void addUserToGroup(Long groupId, Long userId, Handler<AsyncResult<Boolean>> resultHandler);

    void removeUserFromGroup(Long groupId, Long userId, Handler<AsyncResult<Boolean>> resultHandler);

    void queryGroups(Long userId, Handler<AsyncResult<List<UserGroup>>> resultHandler);

    void queryGroupMembers(Long groupId, Handler<AsyncResult<List<UserGroupMember>>> resultHandler);
}


