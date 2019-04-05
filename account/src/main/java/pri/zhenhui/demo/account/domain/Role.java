package pri.zhenhui.demo.account.domain;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pri.zhenhui.demo.account.domain.enums.RoleType;

import java.io.Serializable;

@SuppressWarnings("unused")
@Data
@NoArgsConstructor
@AllArgsConstructor
@DataObject(generateConverter = true)
public class Role implements Serializable {

    private static final long serialVersionUID = 242612129412921209L;

    private long id;

    private String title;

    public Role(JsonObject jsonObj) {
        RoleConverter.fromJson(jsonObj, this);
    }

    public static Role from(RoleType type) {
        return new Role(type.id, type.title);
    }

    public static Role from(long id) {
        return from(RoleType.valueOf(id));
    }

    public JsonObject toJson() {
        JsonObject jsonObj = new JsonObject();
        RoleConverter.toJson(this, jsonObj);
        return jsonObj;
    }
}
