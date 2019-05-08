package pri.zhenhui.demo.udms.dal.mapper;

import org.apache.ibatis.annotations.Param;
import pri.zhenhui.demo.tracer.domain.UniqueID;
import pri.zhenhui.demo.udms.dal.domain.DeviceGroupDO;
import pri.zhenhui.demo.udms.dal.domain.DeviceGroupMemberDO;
import pri.zhenhui.demo.udms.dal.domain.UserDeviceBindDO;

import java.util.List;

public interface DeviceGroupMapper {

    int insertGroup(DeviceGroupDO group);

    List<DeviceGroupDO> selectGroups(@Param("userId") long userId);

    DeviceGroupDO selectGroup(@Param("groupId") long groupId);

    int deleteGroup(@Param("userId") long userId, @Param("groupId") long groupId);

    int insertGroupMember(DeviceGroupMemberDO member);

    List<DeviceGroupMemberDO> selectGroupMembers(@Param("groupId") long groupId);

    int deleteGroupMember(@Param("groupId") long groupId, @Param("deviceId") UniqueID deviceId);

    int insertUserDevice(UserDeviceBindDO bind);

    List<UserDeviceBindDO> selectUserDevice(@Param("userId") long userId);

    int deleteUserDevice(@Param("userId") long userId, @Param("deviceId") UniqueID deviceId);
}
