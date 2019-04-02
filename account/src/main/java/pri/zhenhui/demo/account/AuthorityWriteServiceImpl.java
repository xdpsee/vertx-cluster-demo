package pri.zhenhui.demo.account;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.reactivex.core.Context;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import pri.zhenhui.demo.account.domain.Authority;
import pri.zhenhui.demo.account.domain.Role;
import pri.zhenhui.demo.account.mapper.AuthorityMapper;
import pri.zhenhui.demo.account.mapper.RoleMapper;
import pri.zhenhui.demo.support.SqlSessionFactoryUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AuthorityWriteServiceImpl implements AuthorityWriteService {

    private final Context context;

    private final SqlSessionFactory sqlSessionFactory;

    AuthorityWriteServiceImpl(Context context) {
        this.context = context;
        this.sqlSessionFactory = SqlSessionFactoryUtils.build();
    }

    @Override
    public void createRole(Role role, Handler<AsyncResult<Long>> resultHandler) {
        context.<Long>executeBlocking(future -> {
            final SqlSession session = sqlSessionFactory.openSession();
            try {
                RoleMapper roleMapper = session.getMapper(RoleMapper.class);
                roleMapper.insert(role);
                session.commit();
                future.complete(role.getId());
            } catch (Throwable e) {
                session.rollback();
                future.fail(e);
            } finally {
                session.close();
            }
        }, resultHandler);
    }

    @Override
    public void updateRole(Long roleId, String title, String description, Handler<AsyncResult<Boolean>> resultHandler) {
        final Map<String, Object> params = new HashMap<>();
        params.put("id", roleId);
        params.put("title", title);
        params.put("description", description);

        context.<Boolean>executeBlocking(future -> {
            final SqlSession session = sqlSessionFactory.openSession();
            try {
                RoleMapper roleMapper = session.getMapper(RoleMapper.class);
                int rows = roleMapper.update(params);
                session.commit();
                future.complete(rows > 0);
            } catch (Throwable e) {
                session.rollback();
                future.fail(e);
            } finally {
                session.close();
            }
        }, resultHandler);
    }

    @Override
    public void createAuthority(Authority authority, Handler<AsyncResult<Long>> resultHandler) {
        context.<Long>executeBlocking(future -> {
            final SqlSession session = sqlSessionFactory.openSession();
            try {
                AuthorityMapper authorityMapper = session.getMapper(AuthorityMapper.class);
                authorityMapper.insert(authority);
                session.commit();
                future.complete(authority.getId());
            } catch (Throwable e) {
                session.rollback();
                future.fail(e);
            } finally {
                session.close();
            }
        }, resultHandler);
    }

    @Override
    public void updateAuthority(Long authorityId, String title, String description, Handler<AsyncResult<Boolean>> resultHandler) {
        final Map<String, Object> params = new HashMap<>();
        params.put("id", authorityId);
        params.put("title", title);
        params.put("description", description);

        context.<Boolean>executeBlocking(future -> {
            final SqlSession session = sqlSessionFactory.openSession();
            try {
                AuthorityMapper authorityMapper = session.getMapper(AuthorityMapper.class);
                int rows = authorityMapper.update(params);
                session.commit();
                future.complete(rows > 0);
            } catch (Throwable e) {
                session.rollback();
                future.fail(e);
            } finally {
                session.close();
            }
        }, resultHandler);
    }

    @Override
    public void createUserRoles(Long userId, List<Role> roles, Handler<AsyncResult<Boolean>> resultHandler) {

        context.<Boolean>executeBlocking(future -> {
            if (null == roles || roles.isEmpty()) {
                future.complete(false);
            } else {
                final SqlSession session = sqlSessionFactory.openSession();
                try {
                    RoleMapper roleMapper = session.getMapper(RoleMapper.class);
                    int rows = roleMapper.insertUserRoles(userId, roles.stream().map(Role::getId).collect(Collectors.toList()));
                    session.commit();
                    future.complete(rows > 0);
                } catch (Throwable e) {
                    session.rollback();
                    future.fail(e);
                } finally {
                    session.close();
                }
            }
        }, resultHandler);
    }

    @Override
    public void deleteUserRoles(Long userId, List<Role> roles, Handler<AsyncResult<Boolean>> resultHandler) {
        context.<Boolean>executeBlocking(future -> {
            if (null == roles || roles.isEmpty()) {
                future.complete(false);
            } else {
                final SqlSession session = sqlSessionFactory.openSession();
                try {
                    RoleMapper roleMapper = session.getMapper(RoleMapper.class);
                    int rows = roleMapper.deleteUserRoles(userId, roles.stream().map(Role::getId).collect(Collectors.toList()));
                    session.commit();
                    future.complete(rows > 0);
                } catch (Throwable e) {
                    session.rollback();
                    future.fail(e);
                } finally {
                    session.commit();
                }
            }
        }, resultHandler);
    }

    @Override
    public void createRoleAuthorities(Long roleId, List<Authority> authorities, Handler<AsyncResult<Boolean>> resultHandler) {
        context.<Boolean>executeBlocking(future -> {
            if (null == authorities || authorities.isEmpty()) {
                future.complete(false);
            } else {
                final SqlSession session = sqlSessionFactory.openSession();
                try {
                    AuthorityMapper authorityMapper = session.getMapper(AuthorityMapper.class);
                    int rows = authorityMapper.insertRoleAuthorities(roleId, authorities.stream().map(Authority::getId).collect(Collectors.toList()));
                    session.commit();
                    future.complete(rows > 0);
                } catch (Throwable e) {
                    session.rollback();
                    future.fail(e);
                } finally {
                    session.close();
                }
            }
        }, resultHandler);
    }

    @Override
    public void deleteRoleAuthorities(Long roleId, List<Authority> authorities, Handler<AsyncResult<Boolean>> resultHandler) {
        context.<Boolean>executeBlocking(future -> {
            if (null == authorities || authorities.isEmpty()) {
                future.complete(false);
            } else {
                final SqlSession session = sqlSessionFactory.openSession();
                try {
                    AuthorityMapper authorityMapper = session.getMapper(AuthorityMapper.class);
                    int rows = authorityMapper.deleteRoleAuthorities(roleId, authorities.stream().map(Authority::getId).collect(Collectors.toList()));
                    session.commit();
                    future.complete(rows > 0);
                } catch (Throwable e) {
                    session.rollback();
                    future.fail(e);
                } finally {
                    session.close();
                }
            }
        }, resultHandler);
    }
}
