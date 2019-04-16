package pri.zhenhui.demo.tracer.data.service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import org.apache.ibatis.session.SqlSessionFactory;
import pri.zhenhui.demo.support.SqlSessionFactoryLoader;
import pri.zhenhui.demo.tracer.domain.Position;
import pri.zhenhui.demo.tracer.server.Context;
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

    }
}


