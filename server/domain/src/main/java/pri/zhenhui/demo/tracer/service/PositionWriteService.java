package pri.zhenhui.demo.tracer.service;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import pri.zhenhui.demo.tracer.domain.Position;

@VertxGen
@ProxyGen
public interface PositionWriteService {

    String SERVICE_NAME = "service.position.data.write";

    String SERVICE_ADDRESS = "address.service.position.data.write";

    void savePosition(Position position, Handler<AsyncResult<Boolean>> resultHandler);

}
