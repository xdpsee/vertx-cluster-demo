package pri.zhenhui.demo.tracer;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.vertx.reactivex.core.AbstractVerticle;
import org.apache.ibatis.session.SqlSessionFactory;
import pri.zhenhui.demo.support.DBUtils;
import pri.zhenhui.demo.support.SqlSessionFactoryLoader;
import pri.zhenhui.demo.tracer.data.service.DeviceReadServiceVerticle;
import pri.zhenhui.demo.tracer.data.service.EventWriteServiceVerticle;
import pri.zhenhui.demo.tracer.data.service.PositionWriteServiceVerticle;

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
                .andThen(vertx.rxDeployVerticle(new EventWriteServiceVerticle()))
                .doOnSuccess(ret -> System.out.println("Deploy EventWriteServiceVerticle Ok!"))
                .ignoreElement()
                .andThen(vertx.rxDeployVerticle(new DeviceReadServiceVerticle()))
                .doOnSuccess(ret -> System.out.println("Deploy DeviceReadServiceVerticle Ok!"))
                .ignoreElement()
                .andThen(vertx.rxDeployVerticle(new PositionWriteServiceVerticle()))
                .doOnSuccess(ret -> System.out.println("Deploy PositionWriteServiceVerticle Ok!"))
                .ignoreElement();
    }
}

