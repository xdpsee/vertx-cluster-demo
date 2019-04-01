package pri.zhenhui.demo.account;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Verticle;
import org.apache.ibatis.session.SqlSessionFactory;
import pri.zhenhui.demo.support.SqlSessionFactoryUtils;

public class MainVerticle extends AbstractVerticle {

    private SqlSessionFactory sqlSessionFactory;

    @Override
    public void start(Future<Void> startFuture) throws Exception {

        sqlSessionFactory = SqlSessionFactoryUtils.build();

        deploy(new UserReadServiceVerticle(sqlSessionFactory))
                .compose((v1) -> deploy(new UserWriteServiceVerticle(sqlSessionFactory))
                        .compose((v2) -> deploy(new AuthorityServiceVerticle(sqlSessionFactory)))
                        .setHandler(result -> {
                            if (result.failed()) {
                                startFuture.fail(result.cause());
                            } else {
                                startFuture.complete();
                            }
                        }));

    }

    private Future<Void> deploy(Verticle verticle) {

        Future<Void> future = Future.future();

        vertx.deployVerticle(verticle, deploy -> {
            if (deploy.failed()) {
                future.fail(deploy.cause());
            } else {
                future.complete();
            }
        });

        return future;
    }
}


