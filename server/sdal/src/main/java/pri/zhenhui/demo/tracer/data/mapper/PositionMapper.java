package pri.zhenhui.demo.tracer.data.mapper;

import org.apache.ibatis.annotations.Param;
import pri.zhenhui.demo.tracer.data.domain.PositionDO;
import pri.zhenhui.demo.tracer.domain.UniqueID;

public interface PositionMapper {

    int insert(PositionDO position);

    PositionDO selectLastPos(@Param("deviceId") UniqueID deviceId);
}



