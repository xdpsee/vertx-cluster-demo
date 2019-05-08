package pri.zhenhui.demo.udms.service.impl;

import com.google.common.collect.Maps;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.reactivex.core.Context;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import pri.zhenhui.demo.support.cache.AbstractEhcache;
import pri.zhenhui.demo.support.db.mybatis.SqlSessionFactoryLoader;
import pri.zhenhui.demo.udms.cache.CacheName;
import pri.zhenhui.demo.udms.dal.domain.RoleAuthorityBindDO;
import pri.zhenhui.demo.udms.dal.mapper.AuthorityMapper;
import pri.zhenhui.demo.udms.domain.Authority;
import pri.zhenhui.demo.udms.domain.Role;
import pri.zhenhui.demo.udms.domain.enums.AuthorityType;
import pri.zhenhui.demo.udms.domain.enums.RoleType;
import pri.zhenhui.demo.udms.service.AuthorityService;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class AuthorityServiceImpl implements AuthorityService {

    private final Context context;

    private final SqlSessionFactory sqlSessionFactory;

    private final UserRoleCache userRoleCache = new UserRoleCache();

    private final RoleAuthorityCache roleAuthorityCache = new RoleAuthorityCache();


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
    public void queryAuthorities(Handler<AsyncResult<List<Authority>>> resultHandler) {
        context.<List<Authority>>executeBlocking(future -> {
            future.complete(Arrays.stream(AuthorityType.values()).map(Authority::from).collect(toList()));
        }, resultHandler);
    }

    @Override
    public void queryUserRoles(Long userId, Handler<AsyncResult<List<Role>>> resultHandler) {

        context.<List<Role>>executeBlocking(future -> {
            try {
                future.complete(userRoleCache.get(userId, () -> {
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
    public void queryRoleAuthorities(Long roleId, Handler<AsyncResult<List<Authority>>> resultHandler) {
        context.<List<Authority>>executeBlocking(future -> {
            try {
                future.complete(roleAuthorityCache.get(roleId, () -> {
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

        context.<List<Authority>>executeBlocking(future -> queryUserRoles(userId, queryUserRoles -> {
            if (queryUserRoles.failed()) {
                future.fail(queryUserRoles.cause());
            } else {
                Set<Long> roleIds = queryUserRoles.result().stream().map(Role::getId).collect(Collectors.toSet());
                if (CollectionUtils.isEmpty(roleIds)) {
                    roleIds.add(RoleType.USER.id);
                }

                try {
                    Map<Long, ArrayList<Authority>> arrayListMap = roleAuthorityCache.multiGet(roleIds, (absentRoleIds) -> {
                        try (SqlSession session = sqlSessionFactory.openSession()) {
                            AuthorityMapper authorityMapper = session.getMapper(AuthorityMapper.class);
                            List<RoleAuthorityBindDO> authorities = authorityMapper.selectMultiRoleAuthorities(new ArrayList<>(absentRoleIds));
                            return authorities.stream()
                                    .collect(groupingBy(RoleAuthorityBindDO::getRoleId))
                                    .entrySet()
                                    .stream()
                                    .map(e -> Maps.immutableEntry(e.getKey(), e.getValue().stream().map(v -> Authority.from(v.getAuthorityId())).collect(Collectors.toCollection(ArrayList::new))))
                                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
                        }
                    });

                    List<Authority> authorities = new ArrayList<>(arrayListMap.values().stream().flatMap(ArrayList::stream).collect(Collectors.toSet()));
                    future.complete(authorities);
                } catch (Throwable e) {
                    future.fail(e);
                }
            }
        }), resultHandler);

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

class UserRoleCache extends AbstractEhcache<Long, ArrayList<Role>> {
    UserRoleCache() {
        super(CacheName.USER_ROLE_CACHE);
    }
}

class RoleAuthorityCache extends AbstractEhcache<Long, ArrayList<Authority>> {
    RoleAuthorityCache() {
        super(CacheName.ROLE_AUTHORITY_CACHE);
    }
}