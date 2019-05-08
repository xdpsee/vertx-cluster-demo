package pri.zhenhui.demo.tracer.data.mapper;

import org.apache.ibatis.annotations.Param;
import pri.zhenhui.demo.tracer.data.domain.DeviceDO;
import pri.zhenhui.demo.tracer.domain.UniqueID;

import java.util.List;
import java.util.Map;

public interface DeviceMapper {

    int insert(DeviceDO device);

    DeviceDO selectById(@Param("deviceId") UniqueID deviceId);

    List<DeviceDO> selectByIds(@Param("deviceIds") List<UniqueID> deviceIds);

    int updateAttrs(@Param("deviceId") UniqueID deviceId, @Param("attributes") Map<String, Object> attributes);
}


