package pri.zhenhui.demo.account;

import org.apache.ibatis.session.SqlSessionFactory;
import pri.zhenhui.demo.support.AbstractMicroServiceVerticle;
import pri.zhenhui.demo.support.DBUtils;
import pri.zhenhui.demo.support.SqlSessionFactoryUtils;

public class UserServiceVerticle extends AbstractMicroServiceVerticle<UserService> {

    private SqlSessionFactory sqlSessionFactory;

    public UserServiceVerticle() {
        super(UserService.SERVICE_NAME, UserService.SERVICE_ADDRESS, UserService.class);
    }

    @Override
    public void start() throws Exception {
        sqlSessionFactory = SqlSessionFactoryUtils.build();

        DBUtils.initDatabase(sqlSessionFactory);
    }

    @Override
    protected UserService serviceImpl() {
        return new UserServiceImpl(vertx, sqlSessionFactory);
    }

}






