package pri.zhenhui.demo.tracer.data.type.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import pri.zhenhui.demo.tracer.enums.DeviceStatus;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeviceStatusTypeHandler extends BaseTypeHandler<DeviceStatus> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, DeviceStatus parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.code);
    }

    @Override
    public DeviceStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return DeviceStatus.valueOf(rs.getInt(columnName));
    }

    @Override
    public DeviceStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return DeviceStatus.valueOf(rs.getInt(columnIndex));
    }

    @Override
    public DeviceStatus getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return DeviceStatus.valueOf(cs.getInt(columnIndex));
    }

}


