package pri.zhenhui.demo.account;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.reactivex.core.Context;
import org.apache.ibatis.session.SqlSessionFactory;
import pri.zhenhui.demo.account.domain.Authority;
import pri.zhenhui.demo.account.domain.Role;
import pri.zhenhui.demo.support.SqlSessionFactoryUtils;

import java.util.List;

public class AuthorityWriteServiceImpl implements AuthorityWriteService {

    private final Context context;

    private final SqlSessionFactory sqlSessionFactory;

    AuthorityWriteServiceImpl(Context context) {
        this.context = context;
        this.sqlSessionFactory = SqlSessionFactoryUtils.build();
    }

    @Override
    public void createUserRoles(Long userId, List<Role> roles, Handler<AsyncResult<List<Role>>> resultHandler) {

    }

    @Override
    public void deleteUserRoles(Long userId, List<Role> roles, Handler<AsyncResult<List<Role>>> resultHandler) {

    }

    @Override
    public void createRoleAuthorities(Long roleId, List<Authority> authorities, Handler<AsyncResult<List<Role>>> resultHandler) {

    }

    @Override
    public void deleteRoleAuthorities(Long roleId, List<Authority> authorities, Handler<AsyncResult<List<Role>>> resultHandler) {

    }
}
