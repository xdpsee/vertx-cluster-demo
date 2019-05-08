package pri.zhenhui.demo.udms.domain;

import java.util.Arrays;

public enum Permission {
    TEST(1L, "测试")
    ;

    public final long id;
    public final String comment;

    Permission(long id, String comment) {
        this.id = id;
        this.comment = comment;
    }

    public static Permission valueOf(long id) {
        return Arrays.stream(values())
                .filter(e -> id == e.id).findAny()
                .orElseThrow(() -> new IllegalArgumentException("invalid id " + id));
    }
}

