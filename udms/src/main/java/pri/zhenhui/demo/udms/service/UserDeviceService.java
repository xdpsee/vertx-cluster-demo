package pri.zhenhui.demo.udms.service;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import pri.zhenhui.demo.tracer.domain.Device;
import pri.zhenhui.demo.tracer.domain.UniqueID;

import java.util.List;


@VertxGen
@ProxyGen
public interface UserDeviceService {

    String SERVICE_NAME = "service.data.user.device";

    String SERVICE_ADDRESS = "address.service.data.user.device";

    void assignDevice(long userId, UniqueID deviceId, Handler<AsyncResult<Boolean>> resultHandler);

    void deassignDevice(long userId, UniqueID deviceId, Handler<AsyncResult<Boolean>> resultHandler);

    void queryUserDevices(long userId, Handler<AsyncResult<List<Device>>> resultHandler);
}


