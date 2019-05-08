package pri.zhenhui.demo.udms.domain;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import pri.zhenhui.demo.udms.domain.enums.RoleType;

import java.io.Serializable;

@SuppressWarnings("unused")
@DataObject(generateConverter = true)
public class Role implements Serializable {

    private static final long serialVersionUID = 242612129412921209L;

    private long id;

    private String title;

    public Role() {
    }

    public Role(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Role(JsonObject jsonObj) {
        pri.zhenhui.demo.udms.domain.RoleConverter.fromJson(jsonObj, this);
    }

    public static Role from(RoleType type) {
        return new Role(type.id, type.title);
    }

    public static Role from(long id) {
        return from(RoleType.valueOf(id));
    }

    public JsonObject toJson() {
        JsonObject jsonObj = new JsonObject();
        pri.zhenhui.demo.udms.domain.RoleConverter.toJson(this, jsonObj);
        return jsonObj;
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
