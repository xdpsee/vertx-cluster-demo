package pri.zhenhui.demo.udms.domain;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DataObject(generateConverter = true)
public class Principal implements Serializable {

    private static final long serialVersionUID = 781334796441894148L;

    private Long uid;

    private String sub;

    private List<String> permissions = new ArrayList<>();

    public Principal(JsonObject jsonObj) {
        PrincipalConverter.fromJson(jsonObj, this);
    }

    public JsonObject toJson() {
        JsonObject jsonObj = new JsonObject();
        PrincipalConverter.toJson(this, jsonObj);
        return jsonObj;
    }
}
