package pri.zhenhui.demo.account;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import pri.zhenhui.demo.account.domain.User;
import pri.zhenhui.demo.account.mapper.UserMapper;

public class UserServiceImpl implements UserService {

    private Vertx vertx;

    private SqlSessionFactory sqlSessionFactory;

    UserServiceImpl(Vertx vertx, SqlSessionFactory sqlSessionFactory) {
        this.vertx = vertx;
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public void createUser(User user, Handler<AsyncResult<Boolean>> resultHandler) {

        vertx.<Boolean>executeBlocking(future -> {
            SqlSession session = sqlSessionFactory.openSession();
            try {
                UserMapper userMapper = session.getMapper(UserMapper.class);
                userMapper.insert(user);
                future.complete(true);
            } catch (Exception e) {
                future.fail(e);
            } finally {
                session.close();
            }
        }, resultHandler);

    }

    @Override
    public void updateUser(JsonObject fields, Handler<AsyncResult<Boolean>> resultHandler) {

        vertx.<Boolean>executeBlocking(future -> {
            SqlSession session = sqlSessionFactory.openSession();
            try {
                UserMapper userMapper = session.getMapper(UserMapper.class);
                int rows = userMapper.update(fields.getMap());
                future.complete(rows > 0);
            } catch (Exception e) {
                future.fail(e);
            } finally {
                session.close();
            }
        }, resultHandler);

    }

    @Override
    public void queryUserByName(String username, Handler<AsyncResult<User>> resultHandler) {
        vertx.<User>executeBlocking(future -> {
            SqlSession session = sqlSessionFactory.openSession();
            try {
                UserMapper userMapper = session.getMapper(UserMapper.class);
                User user = userMapper.selectByName(username);
                future.complete(user);
            } catch (Exception e) {
                future.fail(e);
            } finally {
                session.close();
            }
        }, resultHandler);
    }

    @Override
    public void queryUserByPhone(String phone, Handler<AsyncResult<User>> resultHandler) {
        vertx.<User>executeBlocking(future -> {
            SqlSession session = sqlSessionFactory.openSession();
            try {
                UserMapper userMapper = session.getMapper(UserMapper.class);
                User user = userMapper.selectByPhone(phone);
                future.complete(user);
            } catch (Exception e) {
                future.fail(e);
            } finally {
                session.close();
            }
        }, resultHandler);
    }
}
