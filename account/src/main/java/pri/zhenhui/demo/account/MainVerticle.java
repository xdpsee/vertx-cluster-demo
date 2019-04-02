package pri.zhenhui.demo.account;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.vertx.reactivex.core.AbstractVerticle;
import org.apache.ibatis.session.SqlSessionFactory;
import pri.zhenhui.demo.support.DBUtils;
import pri.zhenhui.demo.support.SqlSessionFactoryLoader;

public class MainVerticle extends AbstractVerticle {

    @Override
    public Completable rxStart() {

        return Single.create(emitter -> {
            try {
                SqlSessionFactory sqlSessionFactory = SqlSessionFactoryLoader.load();
                DBUtils.initDatabase(sqlSessionFactory);
                emitter.onSuccess("success");
            } catch (Exception e) {
                emitter.onError(e);
            }
        }).ignoreElement()
                .andThen(vertx.rxDeployVerticle(new UserReadServiceVerticle()))
                .ignoreElement()
                .andThen(vertx.rxDeployVerticle(new UserWriteServiceVerticle()))
                .ignoreElement()
                .andThen(vertx.rxDeployVerticle(new AuthorityReadServiceVerticle()))
                .ignoreElement()
                .andThen(vertx.rxDeployVerticle(new AuthorityWriteServiceVerticle()))
                .ignoreElement();

    }
}

