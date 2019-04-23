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
import pri.zhenhui.demo.tracer.service.PositionWriteService;

public class PositionWriteServiceImpl implements PositionWriteService {

    private final Context context;
    private final SqlSessionFactory sqlSessionFactory;
    private final DeviceLastPosCache deviceLastPosCache;

    public PositionWriteServiceImpl(Context context) {
        this.context = context;
        this.sqlSessionFactory = SqlSessionFactoryLoader.load();
        this.deviceLastPosCache = new DeviceLastPosCache();
    }

    @Override
    public void savePosition(Position position, Handler<AsyncResult<Boolean>> resultHandler) {

        context.executeBlocking(future -> {
            final SqlSession session = sqlSessionFactory.openSession();
            try {
                PositionMapper mapper = session.getMapper(PositionMapper.class);

                PositionDO positionDO = new PositionDO();
                BeanUtils.copyProperties(positionDO, position);
                int rows = mapper.insert(positionDO);
                if (rows > 0) {
                    position.setId(positionDO.getId());
                    if (position.isLocated()) {
                        deviceLastPosCache.put(position.deviceId(), position);
                    }
                }
                session.commit();
                future.complete(rows > 0);
            } catch (Throwable e) {
                session.rollback();
                future.fail(e);
            } finally {
                session.close();
            }
        }, resultHandler);

    }
}


