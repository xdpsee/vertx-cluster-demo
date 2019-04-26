package pri.zhenhui.demo.tracer.data.service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.reactivex.core.Context;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import pri.zhenhui.demo.support.db.mybatis.SqlSessionFactoryLoader;
import pri.zhenhui.demo.tracer.data.domain.EventDO;
import pri.zhenhui.demo.tracer.data.mapper.EventMapper;
import pri.zhenhui.demo.tracer.domain.Event;
import pri.zhenhui.demo.tracer.service.EventWriteService;

import java.util.Date;
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
                    mapper.insert(convert(event));
                }
                session.commit();
                future.complete(true);
            } catch (Throwable e) {
                session.rollback();
                future.fail(e);
            } finally {
                session.close();
            }
        }, resultHandler);

    }

    private EventDO convert(Event event) throws Exception {

        EventDO eventDO = new EventDO();
        BeanUtils.copyProperties(eventDO, event);

        Date now = new Date();
        eventDO.setCreateAt(now);
        eventDO.setUpdateAt(now);

        return eventDO;
    }
}

