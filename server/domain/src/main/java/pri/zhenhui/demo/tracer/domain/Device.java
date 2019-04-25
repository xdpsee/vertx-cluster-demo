package pri.zhenhui.demo.tracer.domain;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pri.zhenhui.demo.tracer.domain.misc.Attributes;
import pri.zhenhui.demo.tracer.enums.DeviceStatus;
import pri.zhenhui.demo.tracer.utils.time.DateUtils;

import java.io.Serializable;
import java.util.Date;

@DataObject(generateConverter = true, inheritConverter = true)
@Data
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class Device extends Attributes implements Serializable {

    @EqualsAndHashCode.Include
    private UniqueID id;

    private String model;

    private String protocol;

    private Date createAt;

    private Date updateAt;

    private DeviceStatus status;

    // 扩展属性
    public static final String KEY_SPEED_LIMIT = "speed.limit";

    public static final String KEY_MAINTENANCE_START = "maintenance.start";

    public static final String KEY_MAINTENANCE_INTERVAL = "maintenance.interval";


    public Device(JsonObject jsonObj) {
        DeviceConverter.fromJson(jsonObj, this);
        //
        setCreateAt(DateUtils.parse(jsonObj.getString("createAt")));
        setUpdateAt(DateUtils.parse(jsonObj.getString("updateAt")));
    }

    public JsonObject toJson() {
        JsonObject jsonObj = new JsonObject();
        DeviceConverter.toJson(this, jsonObj);
        //
        jsonObj.put("createAt", DateUtils.format(createAt));
        jsonObj.put("updateAt", DateUtils.format(updateAt));
        return jsonObj;
    }
}
