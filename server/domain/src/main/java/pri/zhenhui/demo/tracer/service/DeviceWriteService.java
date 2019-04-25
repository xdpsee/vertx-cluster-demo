package pri.zhenhui.demo.tracer.service;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import pri.zhenhui.demo.tracer.domain.Device;

@VertxGen
@ProxyGen
public interface DeviceWriteService {

    String SERVICE_NAME = "service.device.data.write";

    String SERVICE_ADDRESS = "address.service.device.data.write";

    void createDevice(Device device, Handler<AsyncResult<Boolean>> resultHandler);

}
