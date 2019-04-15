package pri.zhenhui.demo.tracer.service;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import pri.zhenhui.demo.tracer.domain.Position;
import pri.zhenhui.demo.tracer.domain.UniqueID;

@VertxGen
@ProxyGen
public interface PositionReadService {

    String SERVICE_NAME = "service.position.data.read";

    String SERVICE_ADDRESS = "address.service.position.data.read";

    void queryLastPosition(UniqueID deviceId, Handler<AsyncResult<Position>> resultHandler);

    void isLastPosition(Position position, Handler<AsyncResult<Boolean>> resultHandler);

}
