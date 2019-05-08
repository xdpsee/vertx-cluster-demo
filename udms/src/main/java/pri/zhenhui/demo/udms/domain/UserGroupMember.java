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
@DataObject
public class UserGroupMember implements Serializable {

    private static final long serialVersionUID = -8219754905420915799L;

    private Long id;

    private String username;

    private String avatar;

    public UserGroupMember(JsonObject jsonObj) {
        this.id = jsonObj.getLong("id");
        this.username = jsonObj.getString("username");
        this.avatar = jsonObj.getString("avatar");
    }

    public JsonObject toJson() {
        return new JsonObject()
                .put("id", id)
                .put("username", username)
                .put("avatar", avatar);
    }

}