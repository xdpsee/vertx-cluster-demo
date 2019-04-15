package pri.zhenhui.demo.tracer.domain;


import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import pri.zhenhui.demo.tracer.domain.misc.Attributes;
import pri.zhenhui.demo.tracer.enums.EventType;

import java.io.Serializable;
import java.util.Date;

@DataObject(generateConverter = true, inheritConverter = true)
public class Event extends Attributes implements Serializable {

    private static final long serialVersionUID = 840651837022709446L;

    private UniqueID deviceId;

    private EventType type;

    private long positionId;

    private Date time;

    public Event() {}

    public Event(EventType type, UniqueID deviceId, long positionId) {
        this.type = type;
        this.positionId = positionId;
        this.time = new Date();
        this.deviceId = deviceId;
    }

    public Event(JsonObject jsonObj) {
        EventConverter.fromJson(jsonObj, this);
    }

    public JsonObject toJson() {
        JsonObject jsonObj = new JsonObject();
        EventConverter.toJson(this, jsonObj);
        return jsonObj;
    }

    // 扩展属性

    public static final String SPEED = "speed";


    // Getter, Setter

    public UniqueID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UniqueID deviceId) {
        this.deviceId = deviceId;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public long getPositionId() {
        return positionId;
    }

    public void setPositionId(long positionId) {
        this.positionId = positionId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}


