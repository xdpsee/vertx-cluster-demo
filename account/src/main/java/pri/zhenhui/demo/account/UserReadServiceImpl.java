package pri.zhenhui.demo.account;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.reactivex.core.Context;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import pri.zhenhui.demo.account.domain.User;
import pri.zhenhui.demo.account.mapper.UserMapper;
import pri.zhenhui.demo.support.SqlSessionFactoryLoader;

public class UserReadServiceImpl implements UserReadService {

    private final Context context;

    private final SqlSessionFactory sqlSessionFactory;

    UserReadServiceImpl(Context context) {
        this.context = context;
        this.sqlSessionFactory = SqlSessionFactoryLoader.load();
    }

    @Override
    public void queryUserByName(String username, Handler<AsyncResult<User>> resultHandler) {
        context.<User>executeBlocking(future -> {
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
        context.<User>executeBlocking(future -> {
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

