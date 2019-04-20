package pri.zhenhui.demo.account.service.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.reactivex.core.Context;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import pri.zhenhui.demo.account.domain.Authority;
import pri.zhenhui.demo.account.domain.Role;
import pri.zhenhui.demo.account.domain.enums.AuthorityType;
import pri.zhenhui.demo.account.domain.enums.RoleType;
import pri.zhenhui.demo.account.mapper.AuthorityMapper;
import pri.zhenhui.demo.account.service.AuthorityService;
import pri.zhenhui.demo.support.SqlSessionFactoryLoader;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class AuthorityServiceImpl implements AuthorityService {

    private final Context context;

    private final SqlSessionFactory sqlSessionFactory;

    public AuthorityServiceImpl(Context context) {
        this.context = context;
        this.sqlSessionFactory = SqlSessionFactoryLoader.load();
    }

    @Override
    public void queryRoles(Handler<AsyncResult<List<Role>>> resultHandler) {
        context.<List<Role>>executeBlocking(future -> {
            future.complete(Arrays.stream(RoleType.values()).map(Role::from).collect(toList()));
        }, resultHandler);
    }

    @Override
    public void queryUserRoles(Long userId, Handler<AsyncResult<List<Role>>> resultHandler) {
        context.<List<Role>>executeBlocking(future -> {
            try (SqlSession session = sqlSessionFactory.openSession()) {
                AuthorityMapper authorityMapper = session.getMapper(AuthorityMapper.class);
                List<Long> roles = authorityMapper.selectUserRoles(userId);
                future.complete(roles.stream().map(Role::from).collect(toList()));
            } catch (Exception e) {
                future.fail(e);
            }
        }, resultHandler);
    }

    @Override
    public void queryAuthorities(Handler<AsyncResult<List<Authority>>> resultHandler) {
        context.<List<Authority>>executeBlocking(future -> {
            future.complete(Arrays.stream(AuthorityType.values()).map(Authority::from).collect(toList()));
        }, resultHandler);
    }

    @Override
    public void queryRoleAuthorities(Long roleId, Handler<AsyncResult<List<Authority>>> resultHandler) {
        context.<List<Authority>>executeBlocking(future -> {
            try (SqlSession session = sqlSessionFactory.openSession()) {
                AuthorityMapper authorityMapper = session.getMapper(AuthorityMapper.class);
                future.complete(authorityMapper.selectRoleAuthorities(roleId).stream()
                        .map(Authority::from)
                        .collect(toList()));
            } catch (Throwable e) {
                future.fail(e);
            }
        }, resultHandler);
    }

    @Override
    public void queryUserAuthorities(Long userId, Handler<AsyncResult<List<Authority>>> resultHandler) {
        context.<List<Authority>>executeBlocking(future -> {
            try (SqlSession session = sqlSessionFactory.openSession()) {
                AuthorityMapper roleMapper = session.getMapper(AuthorityMapper.class);
                List<Long> roles = roleMapper.selectUserRoles(userId);
                if (roles.isEmpty()) {
                    roles.add(RoleType.USER.id);
                }

                AuthorityMapper authorityMapper = session.getMapper(AuthorityMapper.class);
                List<Long> authorities = authorityMapper.selectMultiRoleAuthorities(roles);
                future.complete(authorities.stream().map(Authority::from).collect(toList()));
            } catch (Exception e) {
                future.fail(e);
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
                    AuthorityMapper authorityMapper = session.getMapper(AuthorityMapper.class);
                    int rows = authorityMapper.insertUserRoles(userId, roles.stream().map(Role::getId).collect(Collectors.toList()));
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
                    AuthorityMapper authorityMapper = session.getMapper(AuthorityMapper.class);
                    int rows = authorityMapper.deleteUserRoles(userId, roles.stream().map(Role::getId).collect(Collectors.toList()));
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
