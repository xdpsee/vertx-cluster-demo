package pri.zhenhui.demo.account;

import org.apache.ibatis.session.SqlSessionFactory;
import pri.zhenhui.demo.support.AbstractMicroServiceVerticle;

public class UserWriteServiceVerticle extends AbstractMicroServiceVerticle<UserWriteService> {

    private final SqlSessionFactory sqlSessionFactory;

    public UserWriteServiceVerticle(SqlSessionFactory sessionFactory) {
        super(UserWriteService.SERVICE_NAME, UserWriteService.SERVICE_ADDRESS, UserWriteService.class);
        this.sqlSessionFactory = sessionFactory;
    }

    @Override
    protected UserWriteService serviceImpl() {
        return new UserWriteServiceImpl(vertx, sqlSessionFactory);
    }
}


