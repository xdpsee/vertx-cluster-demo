package pri.zhenhui.demo.uac.service;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import pri.zhenhui.demo.uac.domain.Authority;
import pri.zhenhui.demo.uac.domain.Role;

import java.util.List;

@ProxyGen
@VertxGen
public interface AuthorityService {

    String SERVICE_NAME = "service.data.authority";

    String SERVICE_ADDRESS = "address.service.data.authority";

    void queryRoles(Handler<AsyncResult<List<Role>>> resultHandler);

    void queryUserRoles(Long userId, Handler<AsyncResult<List<Role>>> resultHandler);

    void queryAuthorities(Handler<AsyncResult<List<Authority>>> resultHandler);

    void queryRoleAuthorities(Long roleId, Handler<AsyncResult<List<Authority>>> resultHandler);

    void queryUserAuthorities(Long userId, Handler<AsyncResult<List<Authority>>> resultHandler);

    void createUserRoles(Long userId, List<Role> roles, Handler<AsyncResult<Boolean>> resultHandler);

    void deleteUserRoles(Long userId, List<Role> roles, Handler<AsyncResult<Boolean>> resultHandler);

    void createRoleAuthorities(Long roleId, List<Authority> authorities, Handler<AsyncResult<Boolean>> resultHandler);

    void deleteRoleAuthorities(Long roleId, List<Authority> authorities, Handler<AsyncResult<Boolean>> resultHandler);

}
