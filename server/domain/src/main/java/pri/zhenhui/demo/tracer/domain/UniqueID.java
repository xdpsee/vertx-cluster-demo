package pri.zhenhui.demo.tracer.domain;


import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import lombok.Data;
import lombok.NoArgsConstructor;
import pri.zhenhui.demo.tracer.utils.JsonUtils;

import java.io.Serializable;
import java.util.Arrays;

@DataObject
@Data
@NoArgsConstructor
public class UniqueID implements Serializable {

    private static final long serialVersionUID = -12312335569703L;

    private UniqueType type;

    private String value;

    public UniqueID(UniqueType type, String value) {
        this.type = type;
        this.value = value;
    }

    public UniqueID(JsonObject jsonObj) {
        JsonUtils.fromJson(jsonObj, this);
    }

    public JsonObject toJson() {
        return JsonUtils.toJson(this);
    }

    @Override
    public String toString() {
        return String.format("%s-%s", type.name(), value);
    }


    public static UniqueID valueOf(String unique) {

        int idx = unique.indexOf("-");
        if (idx > 0 && idx < unique.length()) {
            final String[] components = new String[]{
                    unique.substring(0, idx),
                    unique.substring(idx + 1)
            };

            if (Arrays.stream(components).noneMatch(String::isEmpty)) {
                return new UniqueID(UniqueType.valueOf(components[0]), components[1]);
            }
        }

        throw new IllegalArgumentException(String.format("invalid unique-id string: %s", unique));
    }

}
