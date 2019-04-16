package pri.zhenhui.demo.tracer.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import org.apache.commons.beanutils.BeanUtils;

public final class JsonUtils {

    public static <T> void fromJson(JsonObject jsonObj, T obj) {

        T src = Json.decodeValue(jsonObj.toString(), new TypeReference<T>() {});
        try {
            BeanUtils.copyProperties(obj, src);
        } catch (Exception e) {
            throw new IllegalStateException("JsonUtils.fromJson copy properties exception");
        }

    }

    public static <T> JsonObject toJson(T obj) {

        String json = Json.encode(obj);
        return new JsonObject(json);

    }
}
