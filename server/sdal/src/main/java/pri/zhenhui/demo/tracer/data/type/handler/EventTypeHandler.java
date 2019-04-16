package pri.zhenhui.demo.tracer.data.type.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import pri.zhenhui.demo.tracer.enums.EventType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EventTypeHandler extends BaseTypeHandler<EventType> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, EventType parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(1, parameter.code);
    }

    @Override
    public EventType getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return EventType.valueOf(rs.getInt(columnName));
    }

    @Override
    public EventType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return EventType.valueOf(rs.getInt(columnIndex));
    }

    @Override
    public EventType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return EventType.valueOf(cs.getInt(columnIndex));
    }
}



