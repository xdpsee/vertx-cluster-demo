package pri.zhenhui.demo.uac.domain.enums;

import java.util.Arrays;

@SuppressWarnings("unused")
public enum AuthorityType {
    USER_CREATE(1, "创建用户", "创建用户"),
    USER_EDIT(2, "编辑用户", "编辑用户"),
    USER_DELETE(3, "删除用户", "删除用户"),
    USER_VIEW(4, "查看用户信息", "查看用户信息"),

    TODOLIST_CREATE(101, "创建todolist", "创建todolist"),
    TODOLIST_VIEW(102, "查看todolist", "查看todolist"),
    TODOLIST_EDIT(103, "编辑todolist", "编辑todolist"),
    TODOLIST_DELETE(104, "删除todolist", "删除todolist")


    ;

    public final long id;

    public final String title;

    public final String description;

    AuthorityType(long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public static AuthorityType valueOf(long id) {

        return Arrays.stream(values())
                .filter(e -> e.id == id).findAny()
                .orElseThrow(() -> new IllegalArgumentException("invalid authority id: " + id));

    }

}
