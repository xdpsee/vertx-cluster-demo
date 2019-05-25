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
public class UserToken implements Serializable {

    private static final long serialVersionUID = -2419602186018953477L;

    private String token;

    private Principal principal;

    public UserToken(JsonObject jsonObj) {
        UserTokenConverter.fromJson(jsonObj, this);
    }

    public JsonObject toJson() {
        JsonObject jsonObj = new JsonObject();
        UserTokenConverter.toJson(this, jsonObj);
        return jsonObj;
    }

}

