package pri.zhenhui.demo.udms.service.impl;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.reactivex.RxHelper;
import io.vertx.reactivex.core.Context;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.types.EventBusService;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import pri.zhenhui.demo.support.cache.AbstractEhcache;
import pri.zhenhui.demo.support.db.mybatis.SqlSessionFactoryLoader;
import pri.zhenhui.demo.tracer.domain.Device;
import pri.zhenhui.demo.tracer.domain.UniqueID;
import pri.zhenhui.demo.tracer.service.DeviceReadService;
import pri.zhenhui.demo.udms.cache.CacheName;
import pri.zhenhui.demo.udms.dal.domain.UserDeviceBindDO;
import pri.zhenhui.demo.udms.dal.mapper.DeviceGroupMapper;
import pri.zhenhui.demo.udms.service.UserDeviceService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserDeviceServiceImpl implements UserDeviceService {

    private final Context context;

    private final Scheduler scheduler;

    private final ServiceDiscovery serviceDiscovery;

    private final SqlSessionFactory sqlSessionFactory;

    private final UserDeviceIdCache userDeviceIdCache;

    public UserDeviceServiceImpl(Context context, ServiceDiscovery serviceDiscovery) {
        this.context = context;
        this.scheduler = RxHelper.scheduler(context.getDelegate());
        this.serviceDiscovery = serviceDiscovery;
        this.sqlSessionFactory = SqlSessionFactoryLoader.load();
        this.userDeviceIdCache = new UserDeviceIdCache();
    }

    @Override
    public void assignDevice(long userId, UniqueID deviceId, Handler<AsyncResult<Boolean>> resultHandler) {
        context.executeBlocking(future -> {
            SqlSession session = sqlSessionFactory.openSession();
            try {
                DeviceGroupMapper mapper = session.getMapper(DeviceGroupMapper.class);
                UserDeviceBindDO bind = new UserDeviceBindDO(userId, deviceId);
                mapper.insertUserDevice(bind);

                userDeviceIdCache.evict(userId);

                session.commit();
                future.complete(true);
            } catch (Throwable e) {
                session.rollback();
                future.fail(e);
            } finally {
                session.close();
            }
        }, resultHandler);
    }

    @Override
    public void deassignDevice(long userId, UniqueID deviceId, Handler<AsyncResult<Boolean>> resultHandler) {
        context.executeBlocking(future -> {
            SqlSession session = sqlSessionFactory.openSession();
            try {
                DeviceGroupMapper mapper = session.getMapper(DeviceGroupMapper.class);
                mapper.deleteUserDevice(userId, deviceId);

                userDeviceIdCache.evict(userId);

                session.commit();
                future.complete(true);
            } catch (Throwable e) {
                session.rollback();
                future.fail(e);
            } finally {
                session.close();
            }
        }, resultHandler);
    }

    @Override
    public void queryUserDevices(long userId, Handler<AsyncResult<List<Device>>> resultHandler) {

        context.executeBlocking(future -> {
            ServiceReference reference = serviceDiscovery.getReference(EventBusService.createRecord(
                    DeviceReadService.SERVICE_NAME
                    , DeviceReadService.SERVICE_ADDRESS
                    , DeviceReadService.class));

            try {
                DeviceReadService deviceReadService = reference.getAs(DeviceReadService.class);

                listUserDeviceIds(userId).subscribe(deviceIds -> deviceReadService.queryDevices(deviceIds, queryDevices -> {
                    if (queryDevices.failed()) {
                        future.fail(queryDevices.cause());
                    } else {
                        future.complete(queryDevices.result());
                    }
                }), future::fail);

            } catch (Throwable e) {
                future.fail(e);
            } finally {
                reference.release();
            }
        }, resultHandler);
    }

    private Single<List<UniqueID>> listUserDeviceIds(long userId) {
        return Single.<List<UniqueID>>create(emitter -> {
            ArrayList<UniqueID> deviceIds = userDeviceIdCache.get(userId);
            if (deviceIds != null) {
                emitter.onSuccess(deviceIds);
            } else {
                try (SqlSession session = sqlSessionFactory.openSession()) {
                    DeviceGroupMapper mapper = session.getMapper(DeviceGroupMapper.class);
                    ArrayList<UniqueID> result = mapper.selectUserDevice(userId)
                            .stream()
                            .map(UserDeviceBindDO::getDeviceId)
                            .collect(Collectors.toCollection(ArrayList::new));
                    userDeviceIdCache.put(userId, result);

                    emitter.onSuccess(result);
                } catch (Throwable e) {
                    emitter.onError(e);
                }
            }
        }).subscribeOn(scheduler);//MUST
    }
}

class UserDeviceIdCache extends AbstractEhcache<Long/*userId*/, ArrayList<UniqueID>> {

    public UserDeviceIdCache() {
        super(CacheName.USER_DEVICE_ID_CACHE);
    }

}