package pri.zhenhui.demo.tracer.data.type.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import pri.zhenhui.demo.tracer.domain.UniqueID;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UniqueIDTypeHandler extends BaseTypeHandler<UniqueID> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, UniqueID parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.toString());
    }

    @Override
    public UniqueID getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return UniqueID.valueOf(value);
    }

    @Override
    public UniqueID getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return UniqueID.valueOf(value);
    }

    @Override
    public UniqueID getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return UniqueID.valueOf(value);
    }
}
