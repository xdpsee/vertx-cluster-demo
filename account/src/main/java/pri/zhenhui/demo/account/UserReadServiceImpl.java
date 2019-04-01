package pri.zhenhui.demo.account;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import pri.zhenhui.demo.account.domain.User;
import pri.zhenhui.demo.account.mapper.UserMapper;

public class UserReadServiceImpl implements UserReadService {

    private Vertx vertx;

    private SqlSessionFactory sqlSessionFactory;

    UserReadServiceImpl(Vertx vertx, SqlSessionFactory sqlSessionFactory) {
        this.vertx = vertx;
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public void queryUserByName(String username, Handler<AsyncResult<User>> resultHandler) {
        vertx.<User>executeBlocking(future -> {
            try (SqlSession session = sqlSessionFactory.openSession()) {
                UserMapper userMapper = session.getMapper(UserMapper.class);
                User user = userMapper.selectByName(username);
                future.complete(user);
            } catch (Exception e) {
                future.fail(e);
            }
        }, resultHandler);
    }

    @Override
    public void queryUserByPhone(String phone, Handler<AsyncResult<User>> resultHandler) {
        vertx.<User>executeBlocking(future -> {
            try (SqlSession session = sqlSessionFactory.openSession()) {
                UserMapper userMapper = session.getMapper(UserMapper.class);
                User user = userMapper.selectByPhone(phone);
                future.complete(user);
            } catch (Exception e) {
                future.fail(e);
            }
        }, resultHandler);
    }


}

