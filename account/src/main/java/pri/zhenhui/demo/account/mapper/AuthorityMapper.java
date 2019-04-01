package pri.zhenhui.demo.account.mapper;

import org.apache.ibatis.annotations.Param;
import pri.zhenhui.demo.account.domain.Authority;

import java.util.List;
import java.util.Map;

public interface AuthorityMapper {

    int insert(Authority authority);

    List<Authority> selectAll();

    List<Authority> selectByTitles(@Param("titles") List<String> titles);

    List<Authority> selectByRole(@Param("roleIds") List<Long> roleIds);

    int update(Map<String, Object> fields);
}



