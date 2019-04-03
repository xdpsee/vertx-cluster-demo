package pri.zhenhui.demo.account.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AuthorityMapper {

    List<Long> selectUserRoles(@Param("userId") Long userId);

    int insertUserRoles(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);

    int deleteUserRoles(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);

    List<Long> selectRoleAuthorities(@Param("roleId") Long roleId);

    List<Long> selectMultiRoleAuthorities(@Param("roleIds") List<Long> roleId);

    int insertRoleAuthorities(@Param("roleId") Long roleId, @Param("authorityIds") List<Long> authorityIds);

    int deleteRoleAuthorities(@Param("roleId") Long roleId, @Param("authorityIds") List<Long> authorityIds);


}


