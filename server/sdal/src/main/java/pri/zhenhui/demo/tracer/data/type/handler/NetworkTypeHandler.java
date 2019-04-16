package pri.zhenhui.demo.tracer.data.type.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import io.vertx.core.json.Json;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import pri.zhenhui.demo.tracer.domain.misc.Network;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NetworkTypeHandler extends BaseTypeHandler<Network> {


    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Network parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, Json.encode(parameter));
    }

    @Override
    public Network getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return Json.decodeValue(value, new TypeReference<Network>() {
        });
    }

    @Override
    public Network getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return Json.decodeValue(value, new TypeReference<Network>() {
        });
    }

    @Override
    public Network getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return Json.decodeValue(value, new TypeReference<Network>() {
        });
    }
}
