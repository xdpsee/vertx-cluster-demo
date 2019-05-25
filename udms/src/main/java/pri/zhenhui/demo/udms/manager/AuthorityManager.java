package pri.zhenhui.demo.udms.manager;

import com.google.common.collect.Maps;
import io.reactivex.Single;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import pri.zhenhui.demo.support.cache.AbstractEhcache;
import pri.zhenhui.demo.support.db.mybatis.SqlSessionFactoryLoader;
import pri.zhenhui.demo.udms.cache.CacheName;
import pri.zhenhui.demo.udms.dal.domain.RoleAuthorityBindDO;
import pri.zhenhui.demo.udms.dal.mapper.AuthorityMapper;
import pri.zhenhui.demo.udms.domain.Authority;
import pri.zhenhui.demo.udms.domain.Role;
import pri.zhenhui.demo.udms.domain.enums.RoleType;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class AuthorityManager {

    private static volatile AuthorityManager instance;

    private final SqlSessionFactory sqlSessionFactory;

    private final UserRoleCache userRoleCache;

    private final RoleAuthorityCache roleAuthorityCache;

    public synchronized static AuthorityManager getInstance() {
        if (null == instance) {
            synchronized (AuthorityManager.class) {
                if (null == instance) {
                    instance = new AuthorityManager();
                }
            }
        }

        return instance;
    }

    private AuthorityManager() {
        this.sqlSessionFactory = SqlSessionFactoryLoader.load();
        this.userRoleCache = new UserRoleCache();
        this.roleAuthorityCache = new RoleAuthorityCache();
    }

    public Single<List<Role>> getUserRoles(long userId) {
        return Single.create(emitter -> {
            try {
                List<Role> result = userRoleCache.get(userId, () -> {
                    try (SqlSession session = sqlSessionFactory.openSession()) {
                        AuthorityMapper authorityMapper = session.getMapper(AuthorityMapper.class);
                        List<Long> roles = authorityMapper.selectUserRoles(userId);
                        return roles.stream().map(Role::from).collect(toCollection(ArrayList::new));
                    }
                });
                emitter.onSuccess(result.isEmpty() ? Collections.singletonList(Role.from(RoleType.USER)) : result);
            } catch (Throwable e) {
                emitter.onError(e);
            }
        });
    }

    public Single<List<Authority>> getRoleAuthorities(long roleId) {
        return Single.create(emitter -> {
            try {
                List<Authority> result = roleAuthorityCache.get(roleId, () -> {
                    try (SqlSession session = sqlSessionFactory.openSession()) {
                        AuthorityMapper authorityMapper = session.getMapper(AuthorityMapper.class);
                        return authorityMapper.selectRoleAuthorities(roleId).stream()
                                .map(Authority::from)
                                .collect(Collectors.toCollection(ArrayList::new));
                    }
                });
                emitter.onSuccess(result);
            } catch (Throwable e) {
                emitter.onError(e);
            }
        });
    }

    public Single<Map<Long, List<Authority>>> batchGetRoleAuthorities(Set<Long> roleIds) {
        return Single.create(emitter -> {
            try {
                Map<Long, ArrayList<Authority>> result = roleAuthorityCache.multiGet(roleIds, (absentRoleIds) -> {
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
                emitter.onSuccess(new HashMap<>(result));
            } catch (Throwable e) {
                emitter.onError(e);
            }
        });
    }

    public Single<Boolean> createUserRoles(Long userId, List<Role> roles) {
        return Single.create(emitter -> {
            try {
                if (null == roles || roles.isEmpty()) {
                    emitter.onSuccess(false);
                } else {
                    final SqlSession session = sqlSessionFactory.openSession();
                    try {
                        AuthorityMapper authorityMapper = session.getMapper(AuthorityMapper.class);
                        int rows = authorityMapper.insertUserRoles(userId, roles.stream().map(Role::getId).collect(Collectors.toList()));
                        if (rows > 0) {
                            userRoleCache.evict(userId);
                        }

                        session.commit();
                        emitter.onSuccess(rows > 0);
                    } catch (Throwable e) {
                        session.rollback();
                        emitter.onError(e);
                    } finally {
                        session.close();
                    }
                }
            } catch (Throwable e) {
                emitter.onError(e);
            }
        });
    }

    public Single<Boolean> deleteUserRoles(Long userId, List<Role> roles) {
        return Single.create(emitter -> {
            try {
                if (null == roles || roles.isEmpty()) {
                    emitter.onSuccess(false);
                } else {
                    final SqlSession session = sqlSessionFactory.openSession();
                    try {
                        AuthorityMapper authorityMapper = session.getMapper(AuthorityMapper.class);
                        int rows = authorityMapper.deleteUserRoles(userId, roles.stream().map(Role::getId).collect(Collectors.toList()));
                        if (rows > 0) {
                            userRoleCache.evict(userId);
                        }

                        session.commit();
                        emitter.onSuccess(rows > 0);
                    } catch (Throwable e) {
                        session.rollback();
                        emitter.onError(e);
                    } finally {
                        session.close();
                    }
                }
            } catch (Throwable e) {
                emitter.onError(e);
            }
        });
    }

    public Single<Boolean> createRoleAuthorities(Long roleId, List<Authority> authorities) {
        return Single.create(emitter -> {
            if (null == authorities || authorities.isEmpty()) {
                emitter.onSuccess(false);
            } else {
                final SqlSession session = sqlSessionFactory.openSession();
                try {
                    AuthorityMapper authorityMapper = session.getMapper(AuthorityMapper.class);
                    int rows = authorityMapper.insertRoleAuthorities(roleId, authorities.stream().map(Authority::getId).collect(Collectors.toList()));
                    if (rows > 0) {
                        roleAuthorityCache.evict(roleId);
                    }

                    session.commit();
                    emitter.onSuccess(rows > 0);
                } catch (Throwable e) {
                    session.rollback();
                    emitter.onError(e);
                } finally {
                    session.close();
                }
            }
        });
    }


    public Single<Boolean> deleteRoleAuthorities(Long roleId, List<Authority> authorities) {
        return Single.create(emitter -> {
            if (null == authorities || authorities.isEmpty()) {
                emitter.onSuccess(false);
            } else {
                final SqlSession session = sqlSessionFactory.openSession();
                try {
                    AuthorityMapper authorityMapper = session.getMapper(AuthorityMapper.class);
                    int rows = authorityMapper.deleteRoleAuthorities(roleId, authorities.stream().map(Authority::getId).collect(Collectors.toList()));
                    if (rows > 0) {
                        roleAuthorityCache.evict(roleId);
                    }

                    session.commit();
                    emitter.onSuccess(rows > 0);
                } catch (Throwable e) {
                    session.rollback();
                    emitter.onError(e);
                } finally {
                    session.close();
                }
            }
        });
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
