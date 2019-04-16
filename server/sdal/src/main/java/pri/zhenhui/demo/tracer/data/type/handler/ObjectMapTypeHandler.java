package pri.zhenhui.demo.tracer.data.type.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import io.vertx.core.json.Json;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class ObjectMapTypeHandler extends BaseTypeHandler<Map<String, Object>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Map<String, Object> parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, Json.encode(parameter));
    }

    @Override
    public Map<String, Object> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return Json.decodeValue(value, new TypeReference<Map<String, Object>>() {
        });
    }

    @Override
    public Map<String, Object> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return Json.decodeValue(value, new TypeReference<Map<String, Object>>() {
        });
    }

    @Override
    public Map<String, Object> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return Json.decodeValue(value, new TypeReference<Map<String, Object>>() {
        });
    }
}
