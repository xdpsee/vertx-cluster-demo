package pri.zhenhui.demo.udms.service.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.reactivex.core.Context;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import pri.zhenhui.demo.support.cache.AbstractEhcache;
import pri.zhenhui.demo.support.cache.Cache;
import pri.zhenhui.demo.support.db.mybatis.SqlSessionFactoryLoader;
import pri.zhenhui.demo.udms.cache.CacheName;
import pri.zhenhui.demo.udms.dal.domain.UserGroupDO;
import pri.zhenhui.demo.udms.dal.domain.UserGroupMemberDO;
import pri.zhenhui.demo.udms.dal.domain.UserGroupPermissionDO;
import pri.zhenhui.demo.udms.dal.mapper.UserGroupMapper;
import pri.zhenhui.demo.udms.dal.mapper.UserMapper;
import pri.zhenhui.demo.udms.domain.Permission;
import pri.zhenhui.demo.udms.domain.User;
import pri.zhenhui.demo.udms.domain.UserGroup;
import pri.zhenhui.demo.udms.domain.UserGroupMember;
import pri.zhenhui.demo.udms.service.UserGroupService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class UserGroupServiceImpl implements UserGroupService {

    private final Context context;

    private final SqlSessionFactory sqlSessionFactory;

    private final UserGroupCache userGroupCache;

    private final UserGroupMemberCache userGroupMemberCache;

    private final UserGroupPermissionCache userGroupPermissionCache;

    private final Cache<Long, User> userCache;

    public UserGroupServiceImpl(Context context) {
        this.context = context;
        this.sqlSessionFactory = SqlSessionFactoryLoader.load();
        this.userGroupCache = new UserGroupCache();
        this.userGroupMemberCache = new UserGroupMemberCache();
        this.userGroupPermissionCache = new UserGroupPermissionCache();
        this.userCache = new AbstractEhcache<Long, User>(CacheName.USER_CACHE_BY_ID) {
        };
    }

    @Override
    public void createGroup(Long userId, String title, Handler<AsyncResult<Long>> resultHandler) {
        context.executeBlocking(future -> {
            SqlSession session = sqlSessionFactory.openSession();
            try {
                UserGroupMapper mapper = session.getMapper(UserGroupMapper.class);
                UserGroupDO userGroupDO = new UserGroupDO(userId, title);
                mapper.insertGroup(userGroupDO);
                userGroupCache.evict(userId);

                session.commit();
                future.complete(userGroupDO.getId());
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
                UserGroupMapper mapper = session.getMapper(UserGroupMapper.class);
                int rows = mapper.deleteGroup(userId, groupId);
                userGroupCache.evict(userId);

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
    public void assignGroupPermission(Long groupId, Long permission, Handler<AsyncResult<Boolean>> resultHandler) {
        context.executeBlocking(future -> {
            SqlSession session = sqlSessionFactory.openSession();
            try {
                UserGroupMapper mapper = session.getMapper(UserGroupMapper.class);
                UserGroupPermissionDO permissionDO = new UserGroupPermissionDO(groupId, permission);
                permissionDO.setCreateAt(new Date());
                permissionDO.setUpdateAt(new Date());
                int rows = mapper.insertGroupPermission(permissionDO);
                userGroupPermissionCache.evict(groupId);

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
    public void deassignGroupPermission(Long groupId, Long permission, Handler<AsyncResult<Boolean>> resultHandler) {
        context.executeBlocking(future -> {
            SqlSession session = sqlSessionFactory.openSession();
            try {
                UserGroupMapper mapper = session.getMapper(UserGroupMapper.class);
                int rows = mapper.deleteGroupPermissions(groupId, permission);
                userGroupPermissionCache.evict(groupId);

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
    public void queryGroupPermissions(Long groupId, Handler<AsyncResult<List<String>>> resultHandler) {
        context.executeBlocking(future -> {
            try {
                ArrayList<Permission> permissions = userGroupPermissionCache.get(groupId, () -> {
                    try (SqlSession session = sqlSessionFactory.openSession()) {
                        UserGroupMapper mapper = session.getMapper(UserGroupMapper.class);
                        return mapper.selectGroupPermissions(groupId)
                                .stream()
                                .map(UserGroupPermissionDO::getPermissionId)
                                .map(Permission::valueOf)
                                .collect(Collectors.toCollection(ArrayList::new));
                    }
                });

                future.complete(permissions.stream().map(Permission::name).collect(toList()));
            } catch (Throwable e) {
                future.fail(e);
            }
        }, resultHandler);
    }

    @Override
    public void addUserToGroup(Long groupId, Long userId, Handler<AsyncResult<Boolean>> resultHandler) {
        context.executeBlocking(future -> {
            SqlSession session = sqlSessionFactory.openSession();
            try {
                UserGroupMapper mapper = session.getMapper(UserGroupMapper.class);

                UserGroupMemberDO groupMemberDO = new UserGroupMemberDO(groupId, userId);
                int rows = mapper.insertGroupMember(groupMemberDO);

                userGroupMemberCache.evict(groupId);

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
    public void removeUserFromGroup(Long groupId, Long userId, Handler<AsyncResult<Boolean>> resultHandler) {
        context.executeBlocking(future -> {
            SqlSession session = sqlSessionFactory.openSession();
            try {
                UserGroupMapper mapper = session.getMapper(UserGroupMapper.class);
                int rows = mapper.deleteGroupMember(groupId, userId);
                userGroupMemberCache.evict(groupId);

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
    public void queryGroups(Long userId, Handler<AsyncResult<List<UserGroup>>> resultHandler) {
        context.executeBlocking(future -> {
            try {
                ArrayList<UserGroup> groups = userGroupCache.get(userId, () -> {
                    try (SqlSession session = sqlSessionFactory.openSession()) {
                        UserGroupMapper groupMapper = session.getMapper(UserGroupMapper.class);
                        return groupMapper.selectGroups(userId)
                                .stream()
                                .map(e -> new UserGroup(e.getId(), e.getUserId(), e.getTitle()))
                                .collect(Collectors.toCollection(ArrayList::new));
                    }
                });

                future.complete(groups);
            } catch (Throwable e) {
                future.fail(e);
            }
        }, resultHandler);
    }

    @Override
    public void queryGroupMembers(Long groupId, Handler<AsyncResult<List<UserGroupMember>>> resultHandler) {
        context.executeBlocking(future -> {
            try {
                List<UserGroupMember> members = userGroupMemberCache.get(groupId, () -> {
                    try (SqlSession session = sqlSessionFactory.openSession()) {
                        UserMapper userMapper = session.getMapper(UserMapper.class);
                        UserGroupMapper groupMapper = session.getMapper(UserGroupMapper.class);
                        List<Long> memberIds = groupMapper.selectGroupMembers(groupId)
                                .stream()
                                .map(UserGroupMemberDO::getUserId)
                                .collect(toList());
                        return userMapper.selectByIds(memberIds)
                                .stream()
                                .map(e -> new UserGroupMember(e.getId(), e.getUsername(), e.getAvatar()))
                                .collect(Collectors.toCollection(ArrayList::new));
                    }
                });

                future.complete(members);
            } catch (Throwable e) {
                future.fail(e);
            }
        }, resultHandler);
    }
}

class UserGroupCache extends AbstractEhcache<Long/*userId*/, ArrayList<UserGroup>> {
    UserGroupCache() {
        super(CacheName.USER_GROUP_CACHE);
    }
}

class UserGroupMemberCache extends AbstractEhcache<Long/*groupId*/, ArrayList<UserGroupMember>> {
    UserGroupMemberCache() {
        super(CacheName.USER_GROUP_MEMBER_CACHE);
    }
}

class UserGroupPermissionCache extends AbstractEhcache<Long/*groupId*/, ArrayList<Permission>> {
    UserGroupPermissionCache() {
        super(CacheName.USER_GROUP_PERMISSION_CACHE);
    }
}

