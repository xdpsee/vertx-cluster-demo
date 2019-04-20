package pri.zhenhui.demo.uac.domain.enums;

import java.util.Arrays;

@SuppressWarnings("unused")
public enum RoleType {
    USER(1, "普通用户"),
    ADMIN(2, "管理员")
    ;

    public final long id;
    public final String title;
    RoleType(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public static RoleType valueOf(long id) {

        return Arrays.stream(values())
                .filter(e -> e.id == id).findAny()
                .orElseThrow(() -> new IllegalArgumentException("invalid authority id: " + id));

    }

}
