package pri.zhenhui.demo.account;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import pri.zhenhui.demo.account.domain.User;
import pri.zhenhui.demo.account.mapper.UserMapper;

public class UserWriteServiceImpl implements UserWriteService {

    private final Vertx vertx;

    private final SqlSessionFactory sqlSessionFactory;

    UserWriteServiceImpl(Vertx vertx, SqlSessionFactory sqlSessionFactory) {
        this.vertx = vertx;
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public void createUser(User user, Handler<AsyncResult<Boolean>> resultHandler) {

        vertx.<Boolean>executeBlocking(future -> {
            try (SqlSession session = sqlSessionFactory.openSession()) {
                UserMapper userMapper = session.getMapper(UserMapper.class);
                userMapper.insert(user);
                session.commit();
                future.complete(true);
            } catch (Exception e) {
                future.fail(e);
            }
        }, resultHandler);

    }

    @Override
    public void updateUser(JsonObject fields, Handler<AsyncResult<Boolean>> resultHandler) {

        vertx.<Boolean>executeBlocking(future -> {
            try (SqlSession session = sqlSessionFactory.openSession()) {
                UserMapper userMapper = session.getMapper(UserMapper.class);
                int rows = userMapper.update(fields.getMap());
                session.commit();
                future.complete(rows > 0);
            } catch (Exception e) {
                future.fail(e);
            }
        }, resultHandler);

    }
}
