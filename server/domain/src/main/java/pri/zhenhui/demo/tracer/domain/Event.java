package pri.zhenhui.demo.tracer.domain;


import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pri.zhenhui.demo.tracer.domain.misc.Attributes;
import pri.zhenhui.demo.tracer.enums.EventType;
import pri.zhenhui.demo.tracer.utils.time.DateUtils;

import java.io.Serializable;
import java.util.Date;

@DataObject(generateConverter = true, inheritConverter = true)
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Event extends Attributes implements Serializable {

    private static final long serialVersionUID = 840651837022709446L;

    private UniqueID deviceId;

    private EventType type;

    private long positionId;

    private Date time;

    public Event(EventType type, UniqueID deviceId, long positionId, Date time) {
        this.type = type;
        this.positionId = positionId;
        this.time = new Date();
        this.deviceId = deviceId;
        this.time = time;
    }

    public Event(JsonObject jsonObj) {
        EventConverter.fromJson(jsonObj, this);
        setDeviceId(UniqueID.valueOf(jsonObj.getString("deviceId")));
        setTime(DateUtils.parse(jsonObj.getString("time")));
    }

    public JsonObject toJson() {
        JsonObject jsonObj = new JsonObject();
        EventConverter.toJson(this, jsonObj);
        jsonObj.put("deviceId", deviceId.toString());
        jsonObj.put("time", DateUtils.format(time));

        return jsonObj;
    }

    // 扩展属性

    public static final String SPEED = "speed";

}


