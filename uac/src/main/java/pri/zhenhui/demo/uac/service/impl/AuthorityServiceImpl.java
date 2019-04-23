package pri.zhenhui.demo.uac.service.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.reactivex.core.Context;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import pri.zhenhui.demo.support.db.mybatis.SqlSessionFactoryLoader;
import pri.zhenhui.demo.uac.cache.RoleAuthorityCache;
import pri.zhenhui.demo.uac.cache.UserRoleCache;
import pri.zhenhui.demo.uac.domain.Authority;
import pri.zhenhui.demo.uac.domain.Role;
import pri.zhenhui.demo.uac.domain.enums.AuthorityType;
import pri.zhenhui.demo.uac.domain.enums.RoleType;
import pri.zhenhui.demo.uac.mapper.AuthorityMapper;
import pri.zhenhui.demo.uac.service.AuthorityService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;

public class AuthorityServiceImpl implements AuthorityService {

    private final Context context;

    private final SqlSessionFactory sqlSessionFactory;

    private final UserRoleCache userRoleCache = new UserRoleCache();

    private final RoleAuthorityCache roleAuthorityCache = new RoleAuthorityCache();

    private final Cache<Long, List<Authority>> userAuthorityCache;


    public AuthorityServiceImpl(Context context) {
        this.context = context;
        this.sqlSessionFactory = SqlSessionFactoryLoader.load();
        userAuthorityCache = CacheBuilder.newBuilder()
                .expireAfterWrite(3, TimeUnit.MINUTES)
                .maximumSize(10000)
                .build();
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
            try {
                future.complete(userRoleCache.getOrLoad(userId, () -> {
                    try (SqlSession session = sqlSessionFactory.openSession()) {
                        AuthorityMapper authorityMapper = session.getMapper(AuthorityMapper.class);
                        List<Long> roles = authorityMapper.selectUserRoles(userId);
                        return roles.stream().map(Role::from).collect(toCollection(ArrayList::new));
                    }
                }));
            } catch (Throwable e) {
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
            try {
                future.complete(roleAuthorityCache.getOrLoad(roleId, () -> {
                    try (SqlSession session = sqlSessionFactory.openSession()) {
                        AuthorityMapper authorityMapper = session.getMapper(AuthorityMapper.class);
                        return authorityMapper.selectRoleAuthorities(roleId).stream()
                                .map(Authority::from)
                                .collect(Collectors.toCollection(ArrayList::new));
                    }
                }));
            } catch (Throwable e) {
                future.fail(e);
            }
        }, resultHandler);
    }

    @Override
    public void queryUserAuthorities(Long userId, Handler<AsyncResult<List<Authority>>> resultHandler) {

        context.executeBlocking(future -> {
            try {
                future.complete(userAuthorityCache.get(userId, () -> {
                    try (SqlSession session = sqlSessionFactory.openSession()) {
                        AuthorityMapper roleMapper = session.getMapper(AuthorityMapper.class);
                        List<Long> roles = roleMapper.selectUserRoles(userId);
                        if (roles.isEmpty()) {
                            roles.add(RoleType.USER.id);
                        }

                        AuthorityMapper authorityMapper = session.getMapper(AuthorityMapper.class);
                        List<Long> authorities = authorityMapper.selectMultiRoleAuthorities(roles);
                        return authorities.stream().map(Authority::from).collect(toList());
                    }
                }));
            } catch (Throwable e) {
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
                    if (rows > 0) {
                        userRoleCache.evict(userId);
                    }

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
                    if (rows > 0) {
                        userRoleCache.evict(userId);
                    }

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
                    if (rows > 0) {
                        roleAuthorityCache.evict(roleId);
                    }

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
                    if (rows > 0) {
                        roleAuthorityCache.evict(roleId);
                    }

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
