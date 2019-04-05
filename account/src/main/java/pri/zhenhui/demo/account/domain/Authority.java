package pri.zhenhui.demo.account.domain;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pri.zhenhui.demo.account.domain.enums.AuthorityType;

import java.io.Serializable;

@SuppressWarnings("unused")
@Data
@NoArgsConstructor
@AllArgsConstructor
@DataObject(generateConverter = true)
public class Authority implements Serializable {

    private static final long serialVersionUID = 7474327774872249710L;

    private long id;

    private String title;

    private String description;

    public Authority(JsonObject jsonObj) {
        AuthorityConverter.fromJson(jsonObj, this);
    }

    public JsonObject toJson() {
        JsonObject jsonObj = new JsonObject();
        AuthorityConverter.toJson(this, jsonObj);
        return jsonObj;
    }

    public static Authority from(AuthorityType type) {
        return new Authority(type.id, type.title, type.description);
    }

    public static Authority from(long id) {
        return from(AuthorityType.valueOf(id));
    }
}


