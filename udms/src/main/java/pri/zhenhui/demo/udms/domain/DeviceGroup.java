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
public class DeviceGroup implements Serializable {

    private static final long serialVersionUID = 5214133161619731816L;

    private Long id;

    private Long userId;

    private String title;

    public DeviceGroup(JsonObject jsonObj) {
        DeviceGroupConverter.fromJson(jsonObj, this);
    }

    public JsonObject toJson() {
        JsonObject jsonObj = new JsonObject();
        DeviceGroupConverter.toJson(this, jsonObj);
        return jsonObj;
    }

}




