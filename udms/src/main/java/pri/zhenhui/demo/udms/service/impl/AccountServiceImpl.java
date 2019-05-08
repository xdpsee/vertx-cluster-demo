package pri.zhenhui.demo.udms.service.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Context;
import org.apache.commons.beanutils.BeanUtils;
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
import pri.zhenhui.demo.udms.service.AccountService;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

public class AccountServiceImpl implements AccountService {

    private final Context context;

    private final SqlSessionFactory sqlSessionFactory;

    private final UserCache userCache = new UserCache();

    public AccountServiceImpl(Context context) {
        this.context = context;
        this.sqlSessionFactory = SqlSessionFactoryLoader.load();
    }

    @Override
    public void queryUserById(Long userId, Handler<AsyncResult<User>> resultHandler) {

        context.<User>executeBlocking(future -> {
            try {
                future.complete(userCache.getUserById(userId, () -> {
                    try (SqlSession session = sqlSessionFactory.openSession()) {
                        UserMapper userMapper = session.getMapper(UserMapper.class);
                        return convert(userMapper.selectById(userId));
                    }
                }));
            } catch (Throwable e) {
                future.fail(e);
            }
        }, resultHandler);

    }

    @Override
    public void queryUserByIds(Set<Long> userIds, Handler<AsyncResult<List<User>>> resultHandler) {
        context.<List<User>>executeBlocking(future -> {
            try {
                future.complete(new ArrayList<>(userCache.getUserByIds(userIds, (absentUserIds) -> {
                    try (SqlSession session = sqlSessionFactory.openSession()) {
                        UserMapper userMapper = session.getMapper(UserMapper.class);
                        return userMapper.selectByIds(new ArrayList<>(absentUserIds))
                                .stream()
                                .map(this::convert)
                                .collect(toMap(User::getId, Function.identity()));
                    }
                }).values()));
            } catch (Throwable e) {
                future.fail(e);
            }
        }, resultHandler);
    }

    @Override
    public void queryUserByName(String username, Handler<AsyncResult<User>> resultHandler) {

        context.<User>executeBlocking(future -> {
            try {
                future.complete(userCache.getUserByName(username, () -> {
                    try (SqlSession session = sqlSessionFactory.openSession()) {
                        UserMapper userMapper = session.getMapper(UserMapper.class);
                        return convert(userMapper.selectByName(username));
                    }
                }));
            } catch (Throwable e) {
                future.fail(e);
            }
        }, resultHandler);

    }

    @Override
    public void queryUserByPhone(String phone, Handler<AsyncResult<User>> resultHandler) {

        context.<User>executeBlocking(future -> {
            try {
                future.complete(userCache.getUserByPhone(phone, () -> {
                    try (SqlSession session = sqlSessionFactory.openSession()) {
                        UserMapper userMapper = session.getMapper(UserMapper.class);
                        return convert(userMapper.selectByPhone(phone));
                    }
                }));
            } catch (Throwable e) {
                future.fail(e);
            }
        }, resultHandler);

    }

    //TODO: cache optimize ??
    @Override
    public void queryUsers(Long parentId, Handler<AsyncResult<List<User>>> resultHandler) {
        context.<List<User>>executeBlocking(future -> {
            final SqlSession session = sqlSessionFactory.openSession();
            try {
                if (parentId <= 0) {
                    future.complete(new ArrayList<>());
                } else {
                    UserMapper userMapper = session.getMapper(UserMapper.class);
                    List<UserDO> userDOs = userMapper.selectByParent(parentId);
                    future.complete(userDOs.stream().map(this::convert).collect(Collectors.toList()));
                }
            } catch (Throwable e) {
                future.fail(e);
            } finally {
                session.close();
            }
        }, resultHandler);
    }

    @Override
    public void createUser(User user, Handler<AsyncResult<Long>> resultHandler) {

        context.<Long>executeBlocking(future -> {
            final SqlSession session = sqlSessionFactory.openSession();
            try {
                UserMapper userMapper = session.getMapper(UserMapper.class);
                UserDO userDO = reverseConvert(user);
                int rows = userMapper.insert(userDO);
                session.commit();
                future.complete(rows > 0 ? userDO.getId() : 0L);
            } catch (Throwable e) {
                session.rollback();
                future.fail(e);
            } finally {
                session.close();
            }
        }, resultHandler);

    }

    @Override
    public void updateUser(JsonObject fields, Handler<AsyncResult<Boolean>> resultHandler) {

        context.<Boolean>executeBlocking(future -> {
            final SqlSession session = sqlSessionFactory.openSession();
            try {
                UserMapper userMapper = session.getMapper(UserMapper.class);
                int rows = userMapper.update(fields.getMap());
                if (rows > 0) {
                    User user = convert(userMapper.selectById(fields.getLong("id")));
                    userCache.evict(user.getId(), user.getUsername(), user.getPhone());
                }

                session.commit();
                future.complete(rows > 0);
            } catch (Exception e) {
                session.rollback();
                future.fail(e);
            } finally {
                session.close();
            }
        }, resultHandler);

    }

    private User convert(UserDO userDO) {
        return null == userDO ? null : new User(userDO);
    }

    private UserDO reverseConvert(User user) {
        UserDO userDO = new UserDO();

        try {
            BeanUtils.copyProperties(userDO, user);
            userDO.setCreateAt(new Date());
            userDO.setUpdateAt(userDO.getCreateAt());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return userDO;
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
