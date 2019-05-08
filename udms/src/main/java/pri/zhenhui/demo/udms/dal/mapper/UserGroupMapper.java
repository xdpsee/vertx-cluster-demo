package pri.zhenhui.demo.udms.dal.mapper;

import org.apache.ibatis.annotations.Param;
import pri.zhenhui.demo.udms.dal.domain.UserGroupDO;
import pri.zhenhui.demo.udms.dal.domain.UserGroupMemberDO;
import pri.zhenhui.demo.udms.dal.domain.UserGroupPermissionDO;

import java.util.List;

public interface UserGroupMapper {

    // user group
    int insertGroup(UserGroupDO userGroup);

    List<UserGroupDO> selectGroups(@Param("userId") Long userId);

    int deleteGroup(@Param("userId") long userId, @Param("groupId") long groupId);

    // user group member
    int insertGroupMember(UserGroupMemberDO member);

    List<UserGroupMemberDO> selectGroupMembers(@Param("groupId") long groupId);

    int deleteGroupMember(@Param("groupId") long groupId, @Param("userId") long memberId);

    // user group permission
    int insertGroupPermission(UserGroupPermissionDO permission);

    List<UserGroupPermissionDO> selectGroupPermissions(@Param("groupId") long groupId);

    int deleteGroupPermissions(@Param("groupId") long groupId, @Param("permissionId") long permissionId);

}

