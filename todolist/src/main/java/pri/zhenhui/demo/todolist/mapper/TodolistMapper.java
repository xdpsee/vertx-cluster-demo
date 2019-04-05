package pri.zhenhui.demo.todolist.mapper;

import org.apache.ibatis.annotations.Param;
import pri.zhenhui.demo.todolist.domain.Todolist;

import java.util.List;
import java.util.Map;

public interface TodolistMapper {

    int insert(Todolist todolist);

    List<Todolist> selectByPage(@Param("userId") Long userId, @Param("offset")int offset, @Param("limit")int limit);

    int update(Map<String, Object> fields);

    int delete(@Param("todoId") Long todoId);
}
