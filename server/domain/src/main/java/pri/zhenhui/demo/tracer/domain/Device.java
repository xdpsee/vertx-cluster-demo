package pri.zhenhui.demo.tracer.domain;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import pri.zhenhui.demo.tracer.domain.misc.Attributes;

import java.io.Serializable;
import java.util.Date;

@DataObject(generateConverter = true, inheritConverter = true)
public class Device extends Attributes implements Serializable {

    private UniqueID id;

    private String model;

    private String protocol;

    private Date gmtCreate;

    private Date gmtUpdate;

    // 扩展属性
    public static final String KEY_SPEED_LIMIT = "speed.limit";

    public static final String KEY_MAINTENANCE_START = "maintenance.start";

    public static final String KEY_MAINTENANCE_INTERVAL = "maintenance.interval";


    //
    public Device() {}

    public Device(JsonObject jsonObj) {
        DeviceConverter.fromJson(jsonObj, this);
    }

    public JsonObject toJson() {
        JsonObject jsonObj = new JsonObject();
        DeviceConverter.toJson(this, jsonObj);
        return jsonObj;
    }

    // Getter,Setter

    public UniqueID getId() {
        return id;
    }

    public void setId(UniqueID id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtUpdate() {
        return gmtUpdate;
    }

    public void setGmtUpdate(Date gmtUpdate) {
        this.gmtUpdate = gmtUpdate;
    }
}
