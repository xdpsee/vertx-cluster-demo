package pri.zhenhui.demo.tracer;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.vertx.reactivex.core.AbstractVerticle;
import org.apache.ibatis.session.SqlSessionFactory;
import pri.zhenhui.demo.support.DBUtils;
import pri.zhenhui.demo.support.SqlSessionFactoryLoader;
import pri.zhenhui.demo.tracer.data.service.DeviceReadServiceVerticle;

@SuppressWarnings("unused")
public class MainVerticle extends AbstractVerticle {

    @Override
    public Completable rxStart() {

        return Single.create(emitter -> {
            try {
                SqlSessionFactory sqlSessionFactory = SqlSessionFactoryLoader.load();
                DBUtils.initDatabase(sqlSessionFactory);
                emitter.onSuccess(true);
            } catch (Throwable e) {
                emitter.onError(e);
            }
        }).ignoreElement()
                .andThen(vertx.rxDeployVerticle(new DeviceReadServiceVerticle()))
                .ignoreElement();
    }
}

