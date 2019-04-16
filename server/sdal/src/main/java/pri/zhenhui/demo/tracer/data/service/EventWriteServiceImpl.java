package pri.zhenhui.demo.tracer.data.service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.reactivex.core.Context;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import pri.zhenhui.demo.support.SqlSessionFactoryLoader;
import pri.zhenhui.demo.tracer.data.domain.EventDO;
import pri.zhenhui.demo.tracer.data.mapper.EventMapper;
import pri.zhenhui.demo.tracer.domain.Event;
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

        context.executeBlocking(future -> {
            final SqlSession session = sqlSessionFactory.openSession();
            try {
                EventMapper mapper = session.getMapper(EventMapper.class);
                for (Event event : events) {
                    EventDO eventDO = new EventDO();
                    BeanUtils.copyProperties(eventDO, event);
                    mapper.insert(eventDO);
                }
                future.complete(true);
            } catch (Throwable e) {
                session.rollback();
            } finally {
                session.close();
            }
        }, resultHandler);

    }


}

