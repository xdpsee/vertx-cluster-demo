package pri.zhenhui.demo.udms.manager;

import io.reactivex.Single;
import io.vertx.core.json.JsonObject;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import pri.zhenhui.demo.support.cache.AbstractEhcache;
import pri.zhenhui.demo.support.cache.Cache;
import pri.zhenhui.demo.support.cache.CacheBatchLoader;
import pri.zhenhui.demo.support.db.mybatis.SqlSessionFactoryLoader;
import pri.zhenhui.demo.udms.cache.CacheName;
import pri.zhenhui.demo.udms.dal.domain.UserDO;
import pri.zhenhui.demo.udms.dal.mapper.UserMapper;
import pri.zhenhui.demo.udms.domain.User;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

public final class AccountManager {

    private static volatile AccountManager instance;

    private final SqlSessionFactory sqlSessionFactory;

    private final UserCache userCache;

    public synchronized static AccountManager getInstance() {
        if (null == instance) {
            synchronized (AccountManager.class) {
                if (null == instance) {
                    instance = new AccountManager();
                }
            }
        }

        return instance;
    }

    public Single<Long> createUser(User user) {
        return Single.<Long>create(emitter -> {
            final SqlSession session = sqlSessionFactory.openSession();
            try {
                UserMapper userMapper = session.getMapper(UserMapper.class);
                UserDO userDO = reverseConvert(user);
                int rows = userMapper.insert(userDO);
                session.commit();
                emitter.onSuccess(rows > 0 ? userDO.getId() : 0L);
            } catch (Throwable e) {
                session.rollback();
                emitter.onError(e);
            } finally {
                session.close();
            }
        });
    }

    public Single<Boolean> updateUser(JsonObject fields) {
        return Single.<Boolean>create(emitter -> {
            final SqlSession session = sqlSessionFactory.openSession();
            try {
                UserMapper userMapper = session.getMapper(UserMapper.class);
                int rows = userMapper.update(fields.getMap());
                if (rows > 0) {
                    User user = convert(userMapper.selectById(fields.getLong("id")));
                    userCache.evict(user.getId(), user.getUsername(), user.getPhone());
                }
                session.commit();
                emitter.onSuccess(rows > 0);
            } catch (Exception e) {
                session.rollback();
                emitter.onError(e);
            } finally {
                session.close();
            }
        });
    }

    public Single<Optional<User>> getUserById(long userId) {
        return Single.<Optional<User>>create(emitter -> {
            try {
                User user = userCache.getUserById(userId, () -> {
                    try (SqlSession session = sqlSessionFactory.openSession()) {
                        UserMapper userMapper = session.getMapper(UserMapper.class);
                        return convert(userMapper.selectById(userId));
                    }
                });
                emitter.onSuccess(Optional.ofNullable(user));
            } catch (Throwable e) {
                emitter.onError(e);
            }
        });
    }

    public Single<List<User>> getUserByIds(Set<Long> userIds) {
        return Single.<List<User>>create(emitter -> {
            try {
                Map<Long, User> userMap = userCache.getUserByIds(userIds, (absentUserIds) -> {
                    try (SqlSession session = sqlSessionFactory.openSession()) {
                        UserMapper userMapper = session.getMapper(UserMapper.class);
                        return userMapper.selectByIds(new ArrayList<>(absentUserIds))
                                .stream()
                                .map(this::convert)
                                .collect(toMap(User::getId, Function.identity()));
                    }
                });
                emitter.onSuccess(new ArrayList<>(userMap.values()));
            } catch (Throwable e) {
                emitter.onError(e);
            }
        });
    }

    public Single<Optional<User>> getUserByName(String username) {
        return Single.<Optional<User>>create(emitter -> {
            try {
                User user = userCache.getUserByName(username, () -> {
                    try (SqlSession session = sqlSessionFactory.openSession()) {
                        UserMapper userMapper = session.getMapper(UserMapper.class);
                        return convert(userMapper.selectByName(username));
                    }
                });
                emitter.onSuccess(Optional.ofNullable(user));
            } catch (Throwable e) {
                emitter.onError(e);
            }
        });
    }

    public Single<Optional<User>> getUserByPhone(String phone) {
        return Single.<Optional<User>>create(emitter -> {
            try {
                User user = userCache.getUserByPhone(phone, () -> {
                    try (SqlSession session = sqlSessionFactory.openSession()) {
                        UserMapper userMapper = session.getMapper(UserMapper.class);
                        return convert(userMapper.selectByPhone(phone));
                    }
                });
                emitter.onSuccess(Optional.ofNullable(user));
            } catch (Throwable e) {
                emitter.onError(e);
            }
        });
    }

    //TODO: cache optimize ??
    public Single<List<User>> queryUsers(long parentId) {

        return Single.create(emitter -> {
            try (SqlSession session = sqlSessionFactory.openSession()) {
                if (parentId <= 0) {
                    emitter.onSuccess(new ArrayList<>());
                } else {
                    UserMapper userMapper = session.getMapper(UserMapper.class);
                    List<UserDO> userDOs = userMapper.selectByParent(parentId);
                    emitter.onSuccess(userDOs.stream().map(this::convert).collect(Collectors.toList()));
                }
            } catch (Throwable e) {
                emitter.onError(e);
            }
        });
    }

    private User convert(UserDO userDO) {
        if (userDO != null) {
            User user = new User();
            user.setId(userDO.getId());
            user.setParentId(userDO.getParentId());
            user.setUsername(userDO.getUsername());
            user.setAvatar(userDO.getAvatar());
            user.setEmail(userDO.getEmail());
            user.setPhone(userDO.getPhone());
            user.setPassword(userDO.getPassword());
            return user;
        }

        return null;
    }

    private UserDO reverseConvert(User user) {
        if (user != null) {
            UserDO userDO = new UserDO();
            userDO.setParentId(user.getParentId());
            userDO.setUsername(user.getUsername());
            userDO.setAvatar(user.getAvatar());
            userDO.setEmail(user.getEmail());
            userDO.setPhone(user.getPhone());
            userDO.setPassword(user.getPassword());
            userDO.setCreateAt(new Date());
            userDO.setUpdateAt(userDO.getCreateAt());
            return userDO;
        }

        return null;
    }

    private AccountManager() {
        this.sqlSessionFactory = SqlSessionFactoryLoader.load();
        this.userCache = new UserCache();
    }
}

class UserCache {

    private final Cache<Long, User> cacheById;

    private final Cache<String, User> cacheByName;

    private final Cache<String, User> cacheByPhone;

    UserCache() {
        cacheById = new AbstractEhcache<Long, User>(CacheName.USER_CACHE_BY_ID) {
        };
        cacheByName = new AbstractEhcache<String, User>(CacheName.USER_CACHE_BY_NAME) {
        };
        cacheByPhone = new AbstractEhcache<String, User>(CacheName.USER_CACHE_BY_PHONE) {
        };
    }

    User getUserById(Long userId, Callable<User> loader) throws Exception {
        return getUser(cacheById, userId, loader);
    }

    Map<Long, User> getUserByIds(Set<Long> userIds, CacheBatchLoader<Long, User> loader) {
        return cacheById.multiGet(userIds, loader);
    }

    User getUserByName(String username, Callable<User> loader) throws Exception {
        return getUser(cacheByName, username, loader);
    }

    User getUserByPhone(String phone, Callable<User> loader) throws Exception {
        return getUser(cacheByPhone, phone, loader);
    }

    private static <K extends Serializable, V extends Serializable> V getUser(Cache<K, V> cache, K key, Callable<V> loader) throws Exception {

        V user = cache.get(key);
        if (user != null) {
            return user;
        }

        user = loader.call();
        if (user != null) {
            cache.put(key, user);
        }

        return user;

    }

    void evict(Long userId, String username, String phone) {

        if (userId != null) {
            cacheById.evict(userId);
        }

        if (username != null) {
            cacheByName.evict(username);
        }

        if (phone != null) {
            cacheByPhone.evict(phone);
        }
    }


}