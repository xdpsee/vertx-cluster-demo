package pri.zhenhui.demo.account;

import org.apache.ibatis.session.SqlSessionFactory;
import pri.zhenhui.demo.support.AbstractMicroServiceVerticle;

public class AuthorityServiceVerticle extends AbstractMicroServiceVerticle<AuthorityService> {

    private final SqlSessionFactory sqlSessionFactory;

    public AuthorityServiceVerticle(SqlSessionFactory sqlSessionFactory) {
        super(AuthorityService.SERVICE_NAME, AuthorityService.SERVICE_ADDRESS, AuthorityService.class);
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    protected AuthorityService serviceImpl() {
        return new AuthorityServiceImpl(vertx, sqlSessionFactory);
    }
}
