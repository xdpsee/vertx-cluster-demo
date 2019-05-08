package pri.zhenhui.demo.udms.service.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.reactivex.core.Context;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import pri.zhenhui.demo.support.db.mybatis.SqlSessionFactoryLoader;
import pri.zhenhui.demo.tracer.domain.UniqueID;
import pri.zhenhui.demo.udms.dal.domain.DeviceGroupDO;
import pri.zhenhui.demo.udms.dal.domain.DeviceGroupMemberDO;
import pri.zhenhui.demo.udms.dal.mapper.DeviceGroupMapper;
import pri.zhenhui.demo.udms.domain.DeviceGroup;
import pri.zhenhui.demo.udms.service.DeviceGroupService;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class DeviceGroupServiceImpl implements DeviceGroupService {

    private final Context context;

    private final SqlSessionFactory sqlSessionFactory;

    public DeviceGroupServiceImpl(Context context) {
        this.context = context;
        this.sqlSessionFactory = SqlSessionFactoryLoader.load();
    }

    @Override
    public void createGroup(Long userId, String title, Handler<AsyncResult<Long>> resultHandler) {
        context.executeBlocking(future -> {
            SqlSession session = sqlSessionFactory.openSession();
            try {
                DeviceGroupMapper mapper = session.getMapper(DeviceGroupMapper.class);

                DeviceGroupDO deviceGroup = new DeviceGroupDO(userId, title);
                mapper.insertGroup(deviceGroup);
                session.commit();

                future.complete(deviceGroup.getId());
            } catch (Throwable e) {
                session.rollback();
                future.fail(e);
            } finally {
                session.close();
            }
        }, resultHandler);
    }

    @Override
    public void removeGroup(Long userId, Long groupId, Handler<AsyncResult<Boolean>> resultHandler) {
        context.executeBlocking(future -> {
            SqlSession session = sqlSessionFactory.openSession();
            try {
                DeviceGroupMapper mapper = session.getMapper(DeviceGroupMapper.class);

                int rows = mapper.deleteGroup(userId, groupId);
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

    @Override
    public void addDeviceToGroup(Long groupId, UniqueID deviceId, Handler<AsyncResult<Boolean>> resultHandler) {
        context.executeBlocking(future -> {
            SqlSession session = sqlSessionFactory.openSession();
            try {
                DeviceGroupMapper mapper = session.getMapper(DeviceGroupMapper.class);

                final DeviceGroupDO group = mapper.selectGroup(groupId);
                if (group == null) {
                    throw new IllegalArgumentException("device group not found: " + groupId);
                }
                DeviceGroupMemberDO member = new DeviceGroupMemberDO(groupId, deviceId);
                int rows = mapper.insertGroupMember(member);
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

    @Override
    public void removeDeviceFromGroup(Long groupId, UniqueID deviceId, Handler<AsyncResult<Boolean>> resultHandler) {
        context.executeBlocking(future -> {
            SqlSession session = sqlSessionFactory.openSession();
            try {
                DeviceGroupMapper mapper = session.getMapper(DeviceGroupMapper.class);
                final DeviceGroupDO group = mapper.selectGroup(groupId);
                if (group == null) {
                    throw new IllegalArgumentException("device group not found: " + groupId);
                }

                int rows = mapper.deleteGroupMember(groupId, deviceId);
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

    @Override
    public void queryGroups(Long userId, Handler<AsyncResult<List<DeviceGroup>>> resultHandler) {
        context.executeBlocking(future -> {
            try (SqlSession session = sqlSessionFactory.openSession()) {
                DeviceGroupMapper mapper = session.getMapper(DeviceGroupMapper.class);

                future.complete(mapper.selectGroups(userId)
                        .stream()
                        .map(e -> new DeviceGroup(e.getId(), e.getUserId(), e.getTitle()))
                        .collect(toList())
                );
            } catch (Throwable e) {
                future.fail(e);
            }
        }, resultHandler);
    }

    @Override
    public void queryGroupDevices(Long groupId, Handler<AsyncResult<List<UniqueID>>> resultHandler) {
        context.executeBlocking(future -> {
            try (SqlSession session = sqlSessionFactory.openSession()) {
                DeviceGroupMapper mapper = session.getMapper(DeviceGroupMapper.class);

                List<DeviceGroupMemberDO> deviceGroupDOs = mapper.selectGroupMembers(groupId);
                future.complete(deviceGroupDOs.stream()
                        .map(DeviceGroupMemberDO::getDeviceId)
                        .collect(toList())
                );
            } catch (Throwable e) {
                future.fail(e);
            }
        }, resultHandler);
    }
}


