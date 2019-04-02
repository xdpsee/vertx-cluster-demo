package pri.zhenhui.demo.account;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Context;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import pri.zhenhui.demo.account.domain.User;
import pri.zhenhui.demo.account.mapper.UserMapper;
import pri.zhenhui.demo.support.SqlSessionFactoryLoader;

public class UserWriteServiceImpl implements UserWriteService {

    private final Context context;

    private final SqlSessionFactory sqlSessionFactory;

    UserWriteServiceImpl(Context context) {
        this.context = context;
        this.sqlSessionFactory = SqlSessionFactoryLoader.load();
    }

    @Override
    public void createUser(User user, Handler<AsyncResult<Boolean>> resultHandler) {

        context.<Boolean>executeBlocking(future -> {
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

        context.<Boolean>executeBlocking(future -> {
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
