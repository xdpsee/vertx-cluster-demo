package pri.zhenhui.demo.tracer.data.service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.reactivex.core.Context;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import pri.zhenhui.demo.support.SqlSessionFactoryLoader;
import pri.zhenhui.demo.tracer.data.domain.PositionDO;
import pri.zhenhui.demo.tracer.data.mapper.PositionMapper;
import pri.zhenhui.demo.tracer.domain.Position;
import pri.zhenhui.demo.tracer.service.PositionWriteService;

public class PositionWriteServiceImpl implements PositionWriteService {

    private final Context context;
    private final SqlSessionFactory sqlSessionFactory;

    public PositionWriteServiceImpl(Context context) {
        this.context = context;
        this.sqlSessionFactory = SqlSessionFactoryLoader.load();
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
                session.commit();

                position.setId(positionDO.getId());
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


