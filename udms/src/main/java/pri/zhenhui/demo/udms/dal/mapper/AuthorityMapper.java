package pri.zhenhui.demo.udms.dal.mapper;

import org.apache.ibatis.annotations.Param;
import pri.zhenhui.demo.udms.dal.domain.RoleAuthorityBindDO;

import java.util.List;

public interface AuthorityMapper {

    List<Long> selectUserRoles(@Param("userId") Long userId);

    int insertUserRoles(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);

    int deleteUserRoles(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);

    List<Long> selectRoleAuthorities(@Param("roleId") Long roleId);

    List<RoleAuthorityBindDO> selectMultiRoleAuthorities(@Param("roleIds") List<Long> roleId);

    int insertRoleAuthorities(@Param("roleId") Long roleId, @Param("authorityIds") List<Long> authorityIds);

    int deleteRoleAuthorities(@Param("roleId") Long roleId, @Param("authorityIds") List<Long> authorityIds);


}


