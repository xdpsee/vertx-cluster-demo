package pri.zhenhui.demo.account;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import pri.zhenhui.demo.account.domain.Authority;
import pri.zhenhui.demo.account.domain.Role;

import java.util.List;

public interface AuthorityWriteService {

    String SERVICE_NAME = "service.data.authority.write";

    String SERVICE_ADDRESS = "address.service.data.authority.write";

    void createUserRoles(Long userId, List<Role> roles, Handler<AsyncResult<List<Role>>> resultHandler);

    void deleteUserRoles(Long userId, List<Role> roles, Handler<AsyncResult<List<Role>>> resultHandler);

    void createRoleAuthorities(Long roleId, List<Authority> authorities, Handler<AsyncResult<List<Role>>> resultHandler);

    void deleteRoleAuthorities(Long roleId, List<Authority> authorities, Handler<AsyncResult<List<Role>>> resultHandler);

}
