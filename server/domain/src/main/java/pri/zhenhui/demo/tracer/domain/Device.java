package pri.zhenhui.demo.tracer.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import lombok.Data;
import lombok.NoArgsConstructor;
import pri.zhenhui.demo.tracer.domain.misc.Attributes;
import pri.zhenhui.demo.tracer.enums.DeviceStatus;
import pri.zhenhui.demo.tracer.utils.JsonUtils;

import java.io.Serializable;
import java.util.Date;

@DataObject
@Data
@NoArgsConstructor
public class Device extends Attributes implements Serializable {

    private UniqueID id;

    private String model;

    private String protocol;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date createAt;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date updateAt;

    private DeviceStatus status;

    public Device(JsonObject jsonObj) {
        JsonUtils.fromJson(jsonObj, this);
    }

    public JsonObject toJson() {
        return JsonUtils.toJson(this);
    }


    // 扩展属性
    public static final String KEY_SPEED_LIMIT = "speed.limit";

    public static final String KEY_MAINTENANCE_START = "maintenance.start";

    public static final String KEY_MAINTENANCE_INTERVAL = "maintenance.interval";
}
