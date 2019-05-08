package pri.zhenhui.demo.udms.domain;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DataObject(generateConverter = true)
public class UserGroup implements Serializable {

    private Long id;

    private Long userId;

    private String title;

    public UserGroup(JsonObject jsonObj) {
        UserGroupConverter.fromJson(jsonObj, this);
    }

    public JsonObject toJson() {
        JsonObject jsonObj = new JsonObject();
        UserGroupConverter.toJson(this, jsonObj);
        return jsonObj;
    }
}

