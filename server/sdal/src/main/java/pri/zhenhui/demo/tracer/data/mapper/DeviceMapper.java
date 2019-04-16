package pri.zhenhui.demo.tracer.data.mapper;

import org.apache.ibatis.annotations.Param;
import pri.zhenhui.demo.tracer.data.domain.DeviceDO;
import pri.zhenhui.demo.tracer.domain.UniqueID;

import java.util.Map;

public interface DeviceMapper {

    int insert(DeviceDO device);

    DeviceDO selectById(@Param("deviceId") UniqueID deviceId);

    int updateAttrs(@Param("deviceId") UniqueID deviceId, @Param("attributes") Map<String, Object> attributes);
}


