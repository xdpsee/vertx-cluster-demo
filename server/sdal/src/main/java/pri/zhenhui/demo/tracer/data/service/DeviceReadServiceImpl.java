package pri.zhenhui.demo.tracer.data.service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.reactivex.core.Context;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import pri.zhenhui.demo.support.db.mybatis.SqlSessionFactoryLoader;
import pri.zhenhui.demo.tracer.data.cache.DeviceCache;
import pri.zhenhui.demo.tracer.data.domain.DeviceDO;
import pri.zhenhui.demo.tracer.data.mapper.DeviceMapper;
import pri.zhenhui.demo.tracer.domain.Device;
import pri.zhenhui.demo.tracer.domain.UniqueID;
import pri.zhenhui.demo.tracer.service.DeviceReadService;

public class DeviceReadServiceImpl implements DeviceReadService {

    private final Context context;
    private final SqlSessionFactory sqlSessionFactory;
    private final DeviceCache deviceCache;

    public DeviceReadServiceImpl(Context context) {
        this.context = context;
        this.sqlSessionFactory = SqlSessionFactoryLoader.load();
        this.deviceCache = new DeviceCache();
    }

    @Override
    public void queryDevice(UniqueID deviceId, Handler<AsyncResult<Device>> resultHandler) {

        context.<Device>executeBlocking(future -> {
            try {
                future.complete(deviceCache.get(deviceId, () -> {
                    try (SqlSession session = sqlSessionFactory.openSession()) {
                        DeviceMapper deviceMapper = session.getMapper(DeviceMapper.class);
                        try {
                            Device device = convert(deviceMapper.selectById(deviceId));
                            return device;
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }));
            } catch (Throwable e) {
                future.fail(e);
            }
        }, resultHandler);
    }

    private Device convert(DeviceDO deviceDO) throws Exception {
        if (null != deviceDO) {
            Device device = new Device();
            BeanUtils.copyProperties(device, deviceDO);
            return device;
        }

        return null;
    }
}
