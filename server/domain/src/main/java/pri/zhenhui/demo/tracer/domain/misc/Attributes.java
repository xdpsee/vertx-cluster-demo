package pri.zhenhui.demo.tracer.domain.misc;


import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
@DataObject(generateConverter = true)
public class Attributes implements Serializable {

    private static final long serialNumberUID = -1293043694567926L;

    protected Map<String, Object> attributes = new HashMap<>();

    public Attributes() {
    }

    public Attributes(JsonObject jsonObj) {
        AttributesConverter.fromJson(jsonObj, this);
    }

    public JsonObject toJson() {
        JsonObject jsonObj = new JsonObject();
        AttributesConverter.toJson(this, jsonObj);
        return jsonObj;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public boolean hasKey(String key) {
        return attributes.containsKey(key);
    }

    public String getString(String key) {
        if (attributes.containsKey(key)) {
            return (String) attributes.get(key);
        } else {
            return null;
        }
    }

    public double getDouble(String key) {
        if (attributes.containsKey(key)) {
            return ((Number) attributes.get(key)).doubleValue();
        } else {
            return 0.0;
        }
    }

    public double getDouble(String key, double defaultValue) {
        if (attributes.containsKey(key)) {
            return ((Number) attributes.get(key)).doubleValue();
        } else {
            return defaultValue;
        }
    }

    public boolean getBoolean(String key) {
        if (attributes.containsKey(key)) {
            return Boolean.parseBoolean(attributes.get(key).toString());
        } else {
            return false;
        }
    }

    public int getInteger(String key) {
        if (attributes.containsKey(key)) {
            return ((Number) attributes.get(key)).intValue();
        } else {
            return 0;
        }
    }

    public long getLong(String key) {
        if (attributes.containsKey(key)) {
            return ((Number) attributes.get(key)).longValue();
        } else {
            return 0;
        }
    }

    public void set(String key, boolean value) {
        attributes.put(key, value);
    }

    public void set(String key, int value) {
        attributes.put(key, value);
    }

    public void set(String key, long value) {
        attributes.put(key, value);
    }

    public void set(String key, double value) {
        attributes.put(key, value);
    }

    public void set(String key, String value) {
        attributes.put(key, value);
    }

}
