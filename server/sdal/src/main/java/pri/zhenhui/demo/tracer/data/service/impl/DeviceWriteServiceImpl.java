package pri.zhenhui.demo.tracer.data.service.impl;


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
import pri.zhenhui.demo.tracer.service.DeviceWriteService;

public class DeviceWriteServiceImpl implements DeviceWriteService {

    private final Context context;
    private final SqlSessionFactory sqlSessionFactory;
    private final DeviceCache deviceCache;

    public DeviceWriteServiceImpl(Context context) {
        this.context = context;
        this.sqlSessionFactory = SqlSessionFactoryLoader.load();
        this.deviceCache = new DeviceCache();
    }

    @Override
    public void createDevice(Device device, Handler<AsyncResult<Boolean>> resultHandler) {

        context.<Boolean>executeBlocking(future -> {

            final SqlSession session = sqlSessionFactory.openSession();
            try {
                DeviceMapper mapper = session.getMapper(DeviceMapper.class);

                DeviceDO deviceDO = new DeviceDO();
                BeanUtils.copyProperties(deviceDO, device);
                int rows = mapper.insert(deviceDO);
                session.commit();

                future.complete(rows > 0);
            } catch (Throwable e) {
                session.rollback();
                future.fail(e);
            } finally {
                session.close();
            }

        }, resultHandler);

    }
}
