package pri.zhenhui.demo.tracer.enums;

import java.util.Arrays;

public enum CommandType {
    TYPE_CUSTOM(1, "custom"),
    TYPE_IDENTIFICATION(2, "device-identification"),
    TYPE_POSITION_SINGLE(3, "position-single"),
    TYPE_POSITION_PERIODIC(4, "position-periodic"),
    TYPE_POSITION_STOP(5, "position-shutdown"),
    TYPE_ENGINE_STOP(6, "engine-shutdown"),
    TYPE_ENGINE_RESUME(7, "engine-resume"),
    TYPE_ALARM_ARM(8, "alarm-arm"),
    TYPE_ALARM_DISARM(9, "alarm-disarm"),
    TYPE_SET_TIMEZONE(10, "set-timezone"),
    TYPE_REQUEST_PHOTO(11, "request-photo"),
    TYPE_REBOOT_DEVICE(12, "reboot-device"),
    TYPE_SEND_SMS(13, "send-sms"),
    TYPE_SEND_USSD(14, "send-ussd"),
    TYPE_SOS_NUMBER(15, "sos-number"),
    TYPE_SILENCE_TIME(16, "silence-time"),
    TYPE_VOICE_MESSAGE(17, "voice-message"),
    TYPE_OUTPUT_CONTROL(18, "output-control"),
    TYPE_VOICE_MONITORING(19, "voice-monitoring"),
    TYPE_SET_AGPS(20, "set-agps"),
    TYPE_SET_INDICATOR(21, "set-indicator"),
    TYPE_MODE_POWER_SAVING(22, "mode-power-saving"),
    TYPE_MODE_DEEP_SLEEP(23, "mode-deep-sleep"),
    TYPE_ALARM_GEOFENCE(24, "movement-alarm"),
    TYPE_ALARM_BATTERY(25, "alarm-battery"),
    TYPE_ALARM_SOS(26, "alarm-sos"),
    TYPE_ALARM_REMOVE(27, "alarm-remove"),
    TYPE_ALARM_CLOCK(28, "alarm-clock"),
    TYPE_ALARM_SPEED(29, "alarm-speed"),
    TYPE_ALARM_FALL(30, "alarm-fall"),
    TYPE_ALARM_VIBRATION(31, "alarm-vibration"),
    ;

    public final int code;
    public final String comment;

    CommandType(int code, String comment) {
        this.code = code;
        this.comment = comment;
    }

    public static CommandType valueOf(int code) {

        CommandType[] types = values();

        return Arrays.stream(types)
            .filter(type->type.code == code)
            .findAny()
            .orElseThrow(()-> new IllegalArgumentException(String.format("invalid code : %d", code)));
    }

}
