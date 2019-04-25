package pri.zhenhui.demo.tracer.data;


import io.reactivex.Completable;
import io.reactivex.Single;
import io.vertx.reactivex.core.AbstractVerticle;
import org.apache.ibatis.session.SqlSessionFactory;
import pri.zhenhui.demo.support.db.DBUtils;
import pri.zhenhui.demo.support.db.mybatis.SqlSessionFactoryLoader;
import pri.zhenhui.demo.tracer.data.verticles.*;

@SuppressWarnings("unused")
public class MainVerticle extends AbstractVerticle {

    @Override
    public Completable rxStart() {

        return initDB()
                .ignoreElement()
                .andThen(vertx.rxDeployVerticle(new EventWriteServiceVerticle()))
                .doOnSuccess(ret -> System.out.println("Deploy EventWriteServiceVerticle Ok!"))
                .ignoreElement()
                .andThen(vertx.rxDeployVerticle(new DeviceReadServiceVerticle()))
                .doOnSuccess(ret -> System.out.println("Deploy DeviceReadServiceVerticle Ok!"))
                .ignoreElement()
                .andThen(vertx.rxDeployVerticle(new PositionReadServiceVerticle()))
                .doOnSuccess(ret -> System.out.println("Deploy PositionReadServiceVerticle Ok!"))
                .ignoreElement()
                .andThen(vertx.rxDeployVerticle(new PositionWriteServiceVerticle()))
                .doOnSuccess(ret -> System.out.println("Deploy PositionWriteServiceVerticle Ok!"))
                .ignoreElement()
                .andThen(vertx.rxDeployVerticle(new DeviceWriteServiceVerticle()))
                .doOnSuccess(ret -> System.out.println("Deploy DeviceWriteServiceVerticle Ok!"))
                .ignoreElement();
    }

    private Single<String> initDB() {
        return Single.create(emitter -> {
            try {
                SqlSessionFactory sqlSessionFactory = SqlSessionFactoryLoader.load();
                DBUtils.initDatabase(sqlSessionFactory);
                emitter.onSuccess("OK");
            } catch (Throwable e) {
                emitter.onError(e);
            }
        });
    }
}

