package pri.zhenhui.demo.tracer.data.service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import org.apache.ibatis.session.SqlSessionFactory;
import pri.zhenhui.demo.support.SqlSessionFactoryLoader;
import pri.zhenhui.demo.tracer.domain.Event;
import pri.zhenhui.demo.tracer.server.Context;
import pri.zhenhui.demo.tracer.service.EventWriteService;

import java.util.List;

public class EventWriteServiceImpl implements EventWriteService {

    private final Context context;
    private final SqlSessionFactory sqlSessionFactory;

    public EventWriteServiceImpl(Context context) {
        this.context = context;
        this.sqlSessionFactory = SqlSessionFactoryLoader.load();
    }


    @Override
    public void saveEvent(List<Event> events, Handler<AsyncResult<Boolean>> resultHandler) {

    }


}

