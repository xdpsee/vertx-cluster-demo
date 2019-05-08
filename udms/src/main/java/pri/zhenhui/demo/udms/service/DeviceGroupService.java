package pri.zhenhui.demo.udms.service;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import pri.zhenhui.demo.tracer.domain.UniqueID;
import pri.zhenhui.demo.udms.domain.DeviceGroup;

import java.util.List;

@VertxGen
@ProxyGen
public interface DeviceGroupService {

    String SERVICE_NAME = "service.data.device.group";

    String SERVICE_ADDRESS = "address.service.data.device.group";

    void createGroup(Long userId, String title, Handler<AsyncResult<Long>> resultHandler);

    void removeGroup(Long userId, Long groupId, Handler<AsyncResult<Boolean>> resultHandler);

    void addDeviceToGroup(Long groupId, UniqueID deviceId, Handler<AsyncResult<Boolean>> resultHandler);

    void removeDeviceFromGroup(Long groupId, UniqueID deviceId, Handler<AsyncResult<Boolean>> resultHandler);

    void queryGroups(Long userId, Handler<AsyncResult<List<DeviceGroup>>> resultHandler);

    void queryGroupDevices(Long groupId, Handler<AsyncResult<List<UniqueID>>> resultHandler);

}



