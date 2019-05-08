package pri.zhenhui.demo.udms.dal.mapper;

import org.apache.ibatis.annotations.Param;
import pri.zhenhui.demo.udms.dal.domain.UserDO;

import java.util.List;
import java.util.Map;

public interface UserMapper {

    int insert(UserDO user);

    UserDO selectById(@Param("userId") Long userId);

    List<UserDO> selectByIds(@Param("userIds") List<Long> userIds);

    UserDO selectByName(@Param("username") String username);

    UserDO selectByPhone(@Param("phone") String phone);

    List<UserDO> selectByParent(@Param("parentId") long parentId);

    int update(Map<String, Object> fields);
}
