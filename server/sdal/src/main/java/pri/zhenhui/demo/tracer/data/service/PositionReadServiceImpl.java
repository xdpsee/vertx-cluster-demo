package pri.zhenhui.demo.tracer.data.service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import org.apache.ibatis.session.SqlSessionFactory;
import pri.zhenhui.demo.support.SqlSessionFactoryLoader;
import pri.zhenhui.demo.tracer.domain.Position;
import pri.zhenhui.demo.tracer.domain.UniqueID;
import pri.zhenhui.demo.tracer.server.Context;
import pri.zhenhui.demo.tracer.service.PositionReadService;

public class PositionReadServiceImpl implements PositionReadService {

    private final Context context;
    private final SqlSessionFactory sqlSessionFactory;

    public PositionReadServiceImpl(Context context) {
        this.context = context;
        this.sqlSessionFactory = SqlSessionFactoryLoader.load();
    }

    @Override
    public void queryLastPosition(UniqueID deviceId, Handler<AsyncResult<Position>> resultHandler) {

    }

    @Override
    public void isLastPosition(Position position, Handler<AsyncResult<Boolean>> resultHandler) {

    }
}


