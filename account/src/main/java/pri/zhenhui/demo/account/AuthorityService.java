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
public interface AuthorityService {

    String SERVICE_NAME = "service.data.authority";

    String SERVICE_ADDRESS = "address.service.data.authority";

    void queryRoles(List<String> roles, Handler<AsyncResult<List<Role>>> resultHandler);

    void queryUserRoles(Long userId, Handler<AsyncResult<List<Role>>> resultHandler);

    void queryAuthorities(List<String> authorities, Handler<AsyncResult<List<Authority>>> resultHandler);

    void queryUserAuthorities(Long userId, Handler<AsyncResult<List<Authority>>> resultHandler);

}
