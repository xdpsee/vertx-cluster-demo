package pri.zhenhui.demo.uac.service.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Context;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import pri.zhenhui.demo.support.db.mybatis.SqlSessionFactoryLoader;
import pri.zhenhui.demo.uac.cache.UserCache;
import pri.zhenhui.demo.uac.domain.User;
import pri.zhenhui.demo.uac.mapper.UserMapper;
import pri.zhenhui.demo.uac.service.AccountService;

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
                        return userMapper.selectById(userId);
                    }
                }));
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
                        return userMapper.selectByName(username);
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
                        return userMapper.selectByPhone(phone);
                    }
                }));
            } catch (Throwable e) {
                future.fail(e);
            }
        }, resultHandler);

    }

    @Override
    public void createUser(User user, Handler<AsyncResult<Long>> resultHandler) {

        context.<Long>executeBlocking(future -> {
            final SqlSession session = sqlSessionFactory.openSession();
            try {
                UserMapper userMapper = session.getMapper(UserMapper.class);
                int rows = userMapper.insert(user);
                session.commit();
                future.complete(rows > 0 ? user.getId() : 0L);
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
                    User user = userMapper.selectById(fields.getLong("id"));
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

}

