package pri.zhenhui.demo.tracer.enums;

import java.util.Arrays;

public enum DeviceStatus {
    EXPIRED(-1, "过期"),
    NORMAL(0, "正常"),
    LOCKED(1, "锁定"),
    ;

    public final int code;
    public final String comment;

    DeviceStatus(int code, String comment) {
        this.code = code;
        this.comment = comment;
    }

    public static DeviceStatus valueOf(int code) {

        return Arrays.stream(values())
                .filter(e -> e.code == code)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String.format("invalid code : " + code)));

    }
}
