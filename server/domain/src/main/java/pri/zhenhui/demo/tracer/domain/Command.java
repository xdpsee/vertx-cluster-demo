package pri.zhenhui.demo.tracer.domain;


import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import lombok.Data;
import lombok.NoArgsConstructor;
import pri.zhenhui.demo.tracer.domain.misc.Attributes;
import pri.zhenhui.demo.tracer.enums.CommandType;
import pri.zhenhui.demo.tracer.utils.JsonUtils;

@DataObject
@Data
@NoArgsConstructor
public class Command extends Attributes {

    private CommandType type;

    private UniqueID deviceId;

    public static final String KEY_UNIQUE_ID = "unique-id";
    public static final String KEY_FREQUENCY = "frequency";
    public static final String KEY_TIMEZONE = "timezone";
    public static final String KEY_DEVICE_PASSWORD = "device-password";
    public static final String KEY_RADIUS = "radius";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_DATA = "data";

    public Command(JsonObject jsonObj) {
        JsonUtils.fromJson(jsonObj, this);
    }

    public JsonObject toJson() {

        return JsonUtils.toJson(this);
    }
}
