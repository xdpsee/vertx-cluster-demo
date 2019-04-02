package pri.zhenhui.demo.account;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import pri.zhenhui.demo.account.domain.Authority;
import pri.zhenhui.demo.account.domain.Role;

import java.util.List;

@ProxyGen
@VertxGen
public interface AuthorityWriteService {

    String SERVICE_NAME = "service.data.authority.write";

    String SERVICE_ADDRESS = "address.service.data.authority.write";

    void createRole(Role role, Handler<AsyncResult<Long>> resultHandler);

    void updateRole(Long roleId, String title, String description, Handler<AsyncResult<Boolean>> resultHandler);

    void createAuthority(Authority authority, Handler<AsyncResult<Long>> resultHandler);

    void updateAuthority(Long authorityId, String title, String description, Handler<AsyncResult<Boolean>> resultHandler);

    void createUserRoles(Long userId, List<Role> roles, Handler<AsyncResult<Boolean>> resultHandler);

    void deleteUserRoles(Long userId, List<Role> roles, Handler<AsyncResult<Boolean>> resultHandler);

    void createRoleAuthorities(Long roleId, List<Authority> authorities, Handler<AsyncResult<Boolean>> resultHandler);

    void deleteRoleAuthorities(Long roleId, List<Authority> authorities, Handler<AsyncResult<Boolean>> resultHandler);

}
