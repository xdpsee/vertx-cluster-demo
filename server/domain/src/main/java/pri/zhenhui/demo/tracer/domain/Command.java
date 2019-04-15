package pri.zhenhui.demo.tracer.domain;


import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import pri.zhenhui.demo.tracer.domain.misc.Attributes;
import pri.zhenhui.demo.tracer.enums.CommandType;

@DataObject(generateConverter = true, inheritConverter = true)
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

    public Command() {}

    public Command(JsonObject jsonObj) {
        CommandConverter.fromJson(jsonObj, this);
    }

    public JsonObject toJson() {

        JsonObject jsonObj = new JsonObject();

        CommandConverter.toJson(this, jsonObj);

        return jsonObj;
    }

    public CommandType getType() {
        return type;
    }

    public void setType(CommandType type) {
        this.type = type;
    }

    public UniqueID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UniqueID deviceId) {
        this.deviceId = deviceId;
    }
}
