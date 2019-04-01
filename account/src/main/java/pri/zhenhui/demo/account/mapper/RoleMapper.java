package pri.zhenhui.demo.account.mapper;

import org.apache.ibatis.annotations.Param;
import pri.zhenhui.demo.account.domain.Role;

import java.util.List;
import java.util.Map;

public interface RoleMapper {

    int insert(Role role);

    List<Role> selectAll();

    List<Role> selectByUser(@Param("userId") Long userId);

    List<Role> selectByTitles(@Param("titles") List<String> titles);

    int update(Map<String, Object> fields);

}
