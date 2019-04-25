package pri.zhenhui.demo.tracer.data.service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.reactivex.core.Context;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import pri.zhenhui.demo.support.db.mybatis.SqlSessionFactoryLoader;
import pri.zhenhui.demo.tracer.data.cache.DeviceLastPosCache;
import pri.zhenhui.demo.tracer.data.domain.PositionDO;
import pri.zhenhui.demo.tracer.data.mapper.PositionMapper;
import pri.zhenhui.demo.tracer.domain.Position;
import pri.zhenhui.demo.tracer.domain.UniqueID;
import pri.zhenhui.demo.tracer.service.PositionReadService;

public class PositionReadServiceImpl implements PositionReadService {

    private final Context context;
    private final SqlSessionFactory sqlSessionFactory;
    private final DeviceLastPosCache deviceLastPosCache;

    public PositionReadServiceImpl(Context context) {
        this.context = context;
        this.sqlSessionFactory = SqlSessionFactoryLoader.load();
        this.deviceLastPosCache = new DeviceLastPosCache();
    }

    @Override
    public void queryLastPosition(UniqueID deviceId, Handler<AsyncResult<Position>> resultHandler) {

        context.executeBlocking(future -> {
            try {
                future.complete(deviceLastPosCache.get(deviceId, () -> {
                    try (SqlSession session = sqlSessionFactory.openSession()) {
                        PositionMapper mapper = session.getMapper(PositionMapper.class);
                        return convert(mapper.selectLastPos(deviceId));
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                }));
            } catch (Throwable e) {
                future.fail(e);
            }
        }, resultHandler);
    }

    @Override
    public void isLastPosition(Position position, Handler<AsyncResult<Boolean>> resultHandler) {
        context.<Boolean>executeBlocking(future -> {
            try {
                final UniqueID deviceId = position.getDeviceId();
                Position lastPos = deviceLastPosCache.get(deviceId, () -> {
                    try (SqlSession session = sqlSessionFactory.openSession()) {
                        PositionMapper mapper = session.getMapper(PositionMapper.class);
                        return convert(mapper.selectLastPos(deviceId));
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                });
                future.complete(lastPos != null && lastPos.getId().equals(position.getId()));
            } catch (Throwable e) {
                future.fail(e);
            }
        }, resultHandler);
    }

    private Position convert(PositionDO positionDO) throws Exception {

        if (positionDO != null) {
            Position position = new Position();
            BeanUtils.copyProperties(position, positionDO);
            return position;
        }

        return null;
    }
}


