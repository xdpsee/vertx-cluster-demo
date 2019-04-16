package pri.zhenhui.demo.tracer.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import lombok.Data;
import lombok.NoArgsConstructor;
import pri.zhenhui.demo.tracer.domain.misc.Attributes;
import pri.zhenhui.demo.tracer.enums.EventType;
import pri.zhenhui.demo.tracer.utils.JsonUtils;

import java.io.Serializable;
import java.util.Date;

@DataObject
@Data
@NoArgsConstructor
public class Event extends Attributes implements Serializable {

    private static final long serialVersionUID = 840651837022709446L;

    private UniqueID deviceId;

    private EventType type;

    private long positionId;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date time;

    public Event(EventType type, UniqueID deviceId, long positionId) {
        this.type = type;
        this.positionId = positionId;
        this.time = new Date();
        this.deviceId = deviceId;
    }

    public Event(JsonObject jsonObj) {
        JsonUtils.fromJson(jsonObj, this);
    }

    public JsonObject toJson() {
        return JsonUtils.toJson(this);
    }

    // 扩展属性

    public static final String SPEED = "speed";

}


