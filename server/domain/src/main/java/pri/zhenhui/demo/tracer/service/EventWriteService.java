package pri.zhenhui.demo.tracer.service;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import pri.zhenhui.demo.tracer.domain.Event;

import java.util.List;

@VertxGen
@ProxyGen
public interface EventWriteService {

    String SERVICE_NAME = "service.event.data.write";

    String SERVICE_ADDRESS = "address.service.event.data.write";

    void saveEvent(List<Event> events, Handler<AsyncResult<Boolean>> resultHandler);

}
