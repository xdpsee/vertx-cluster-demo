package pri.zhenhui.demo.tracer.data.service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.reactivex.core.Context;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import pri.zhenhui.demo.support.SqlSessionFactoryLoader;
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
            Position lastPos = deviceLastPosCache.get(deviceId);
            if (lastPos != null) {
                future.complete(lastPos);
            } else try (SqlSession session = sqlSessionFactory.openSession()) {
                PositionMapper mapper = session.getMapper(PositionMapper.class);
                lastPos = convert(mapper.selectLastPos(deviceId));
                if (lastPos != null) {
                    deviceLastPosCache.put(deviceId, lastPos);
                }
                future.complete(lastPos);
            } catch (Throwable e) {
                future.fail(e);
            }
        }, resultHandler);
    }

    @Override
    public void isLastPosition(Position position, Handler<AsyncResult<Boolean>> resultHandler) {
        context.executeBlocking(future -> {
            Position lastPos = deviceLastPosCache.get(position.getDeviceId());
            if (lastPos != null) {
                future.complete(lastPos.getId().equals(position.getId()));
            } else try (SqlSession session = sqlSessionFactory.openSession()) {
                PositionMapper mapper = session.getMapper(PositionMapper.class);
                lastPos = convert(mapper.selectLastPos(position.getDeviceId()));
                if (lastPos != null) {
                    deviceLastPosCache.put(position.getDeviceId(), lastPos);
                }
                future.complete(lastPos.getId().equals(position.getId()));
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


