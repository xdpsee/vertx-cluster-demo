package pri.zhenhui.demo.account.mapper;

import org.apache.ibatis.annotations.Param;
import pri.zhenhui.demo.account.domain.User;

import java.util.Map;

public interface UserMapper {

    int insert(User user);

    User selectById(@Param("userId") Long userId);

    User selectByName(@Param("username") String username);

    User selectByPhone(@Param("phone") String phone);

    int update(Map<String, Object> fields);
}
