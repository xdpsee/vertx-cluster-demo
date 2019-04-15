package pri.zhenhui.demo.tracer.data.domain;

import lombok.Data;
import pri.zhenhui.demo.tracer.domain.UniqueID;
import pri.zhenhui.demo.tracer.enums.EventType;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class EventDO extends BaseDO implements Serializable {

    private static final long serialVersionUID = 1729444952247959902L;

    private UniqueID deviceId;

    private Date time;

    private EventType type;

    private long positionId;

    private Map<String, Object> attributes = new HashMap<>();

}



