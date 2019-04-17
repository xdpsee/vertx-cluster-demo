package pri.zhenhui.demo.tracer.data.mapper;

import pri.zhenhui.demo.tracer.data.domain.PositionDO;
import pri.zhenhui.demo.tracer.domain.UniqueID;

public interface PositionMapper {

    int insert(PositionDO position);

    PositionDO selectLastPos(UniqueID deviceId);
}



