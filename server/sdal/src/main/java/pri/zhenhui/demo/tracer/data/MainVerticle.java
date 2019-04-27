package pri.zhenhui.demo.tracer.data;


import io.reactivex.Completable;
import io.reactivex.Single;
import io.vertx.reactivex.core.AbstractVerticle;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import pri.zhenhui.demo.support.db.DBUtils;
import pri.zhenhui.demo.support.db.mybatis.SqlSessionFactoryLoader;
import pri.zhenhui.demo.tracer.data.domain.DeviceDO;
import pri.zhenhui.demo.tracer.data.mapper.DeviceMapper;
import pri.zhenhui.demo.tracer.data.verticles.*;
import pri.zhenhui.demo.tracer.domain.UniqueID;
import pri.zhenhui.demo.tracer.enums.DeviceStatus;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;

@SuppressWarnings("unused")
public class MainVerticle extends AbstractVerticle {

    @Override
    public Completable rxStart() {

        return initDB()
                .ignoreElement()
                .andThen(presetDevice())
                .ignoreElement()
                .andThen(vertx.rxDeployVerticle(new DeviceReadServiceVerticle()))
                .ignoreElement()
                .andThen(vertx.rxDeployVerticle(new PositionReadServiceVerticle()))
                .ignoreElement()
                .andThen(vertx.rxDeployVerticle(new PositionWriteServiceVerticle()))
                .ignoreElement()
                .andThen(vertx.rxDeployVerticle(new EventWriteServiceVerticle()))
                .ignoreElement()
                .andThen(vertx.rxDeployVerticle(new DeviceWriteServiceVerticle()))
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

    private Single<String> presetDevice() {
        return Single.create(emitter -> {
            SqlSessionFactory sqlSessionFactory = SqlSessionFactoryLoader.load();
            try (SqlSession session = sqlSessionFactory.openSession()) {
                DeviceMapper mapper = session.getMapper(DeviceMapper.class);

                DeviceDO deviceDO = new DeviceDO();
                deviceDO.setId(UniqueID.valueOf("IMEI-888888888888888"));
                deviceDO.setModel("mobile-test");
                deviceDO.setProtocol("mobile");
                deviceDO.setStatus(DeviceStatus.NORMAL);
                deviceDO.setCreateAt(new Date());
                deviceDO.setUpdateAt(new Date());

                mapper.insert(deviceDO);

                session.commit();

                emitter.onSuccess("Ok");
            } catch (Throwable e) {
                if (e instanceof PersistenceException
                        && e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                    emitter.onSuccess("Ok");
                } else {
                    emitter.onError(e);
                }
            }
        });
    }
}

