package pri.zhenhui.demo.account;

import org.apache.ibatis.session.SqlSessionFactory;
import pri.zhenhui.demo.support.AbstractMicroServiceVerticle;

public class UserReadServiceVerticle extends AbstractMicroServiceVerticle<UserReadService> {

    private final SqlSessionFactory sqlSessionFactory;

    public UserReadServiceVerticle(SqlSessionFactory sessionFactory) {
        super(UserReadService.SERVICE_NAME, UserReadService.SERVICE_ADDRESS, UserReadService.class);
        this.sqlSessionFactory = sessionFactory;
    }

    @Override
    protected UserReadService serviceImpl() {
        return new UserReadServiceImpl(vertx, sqlSessionFactory);
    }
}

