package pri.zhenhui.demo.tracer.service;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import pri.zhenhui.demo.tracer.domain.Device;
import pri.zhenhui.demo.tracer.domain.UniqueID;

@VertxGen
@ProxyGen
public interface DeviceReadService {

    String SERVICE_NAME = "service.device.data.read";

    String SERVICE_ADDRESS = "address.service.device.data.read";

    void queryDevice(UniqueID deviceId, Handler<AsyncResult<Device>> resultHandler);

}


