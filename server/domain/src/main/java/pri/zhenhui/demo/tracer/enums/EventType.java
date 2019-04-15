package pri.zhenhui.demo.tracer.enums;

import java.security.InvalidParameterException;
import java.util.Arrays;

public enum EventType {

    TYPE_COMMAND_RESULT(1, "command-result"),
    TYPE_DEVICE_ONLINE(2, "device-online"),
    TYPE_DEVICE_UNKNOWN(3, "device-unknown"),
    TYPE_DEVICE_OFFLINE(4, "device-offline"),
    TYPE_DEVICE_MOVING(5, "device-moving"),
    TYPE_DEVICE_STOPPED(6, "device-stopped"),
    TYPE_DEVICE_OVERSPEED(7, "device-overspeed"),
    TYPE_GEOFENCE_ENTER(8, "geofence-enter"),
    TYPE_GEOFENCE_EXIT(9, "geofence-exit"),
    TYPE_IGNITION_ON(10, "ignition-on"),
    TYPE_IGNITION_OFF(11, "ignition-off"),
    TYPE_MAINTENANCE(12, "maintenance"),
    TYPE_ALARM(13, "alarm"),
    ;

    public final int code;
    public final String comment;

    EventType(int code, String comment) {
        this.code = code;
        this.comment = comment;
    }

    public static EventType valueOf(int code) {
        EventType[] types = values();
        return Arrays.stream(types)
                .filter(type->type.code == code)
                .findAny()
                .orElseThrow(()->new InvalidParameterException(String.format("invalid code : %d", code)));
    }
}
