package pri.zhenhui.demo.tracer.data.service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.apache.ibatis.session.SqlSessionFactory;
import pri.zhenhui.demo.support.SqlSessionFactoryLoader;
import pri.zhenhui.demo.tracer.domain.Device;
import pri.zhenhui.demo.tracer.domain.UniqueID;
import pri.zhenhui.demo.tracer.service.DeviceReadService;

import java.util.Set;

public class DeviceReadServiceImpl implements DeviceReadService {

    private final Context context;
    private final SqlSessionFactory sqlSessionFactory;

    public DeviceReadServiceImpl(Context context) {
        this.context = context;
        this.sqlSessionFactory = SqlSessionFactoryLoader.load();
    }

    @Override
    public void queryDevice(UniqueID deviceId, Handler<AsyncResult<Device>> resultHandler) {



    }

    @Override
    public void supportedCommands(UniqueID deviceId, Handler<AsyncResult<Set<String>>> resultHandler) {



    }
}
