package pri.zhenhui.demo.account.domain;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import pri.zhenhui.demo.account.domain.enums.RoleType;

import java.io.Serializable;

@SuppressWarnings("unused")
@DataObject(generateConverter = true)
public class Role implements Serializable {

    private static final long serialVersionUID = 242612129412921209L;

    private long id;

    private String title;

    public Role() {}

    public Role(JsonObject jsonObj) {

    }

    public static Role from(RoleType type) {
        Role role = new Role();
        role.id = type.id;
        role.title = type.title;

        return role;
    }

    public static Role from(long id) {
        return from(RoleType.valueOf(id));
    }

    public JsonObject toJson() {
        return null;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
