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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

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

    @Override
    public void queryDevices(List<UniqueID> deviceIds, Handler<AsyncResult<List<Device>>> resultHandler) {

        context.<List<Device>>executeBlocking(future -> {
            try {
                future.complete(new ArrayList<>(deviceCache.multiGet(new HashSet<>(deviceIds), (absentIds) -> {
                    try (SqlSession session = sqlSessionFactory.openSession()) {
                        DeviceMapper deviceMapper = session.getMapper(DeviceMapper.class);
                        return deviceMapper.selectByIds(new ArrayList<>(absentIds))
                                .stream()
                                .map(this::convert)
                                .filter(Objects::nonNull)
                                .collect(toMap(Device::getId, Function.identity()));
                    }
                }).values()));
            } catch (Throwable e) {
                future.fail(e);
            }
        }, resultHandler);

    }

    private Device convert(DeviceDO deviceDO) {
        if (null != deviceDO) {
            try {
                Device device = new Device();
                BeanUtils.copyProperties(device, deviceDO);
                return device;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }
}
