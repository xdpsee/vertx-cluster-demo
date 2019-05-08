package pri.zhenhui.demo.udms.domain;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import pri.zhenhui.demo.udms.domain.enums.AuthorityType;

import java.io.Serializable;

@SuppressWarnings("unused")
@DataObject(generateConverter = true)
public class Authority implements Serializable {

    private static final long serialVersionUID = 7474327774872249710L;

    private long id;

    private String title;

    private String description;

    public Authority() {
    }

    public Authority(long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public Authority(JsonObject jsonObj) {
        pri.zhenhui.demo.udms.domain.AuthorityConverter.fromJson(jsonObj, this);
    }

    public JsonObject toJson() {
        JsonObject jsonObj = new JsonObject();
        pri.zhenhui.demo.udms.domain.AuthorityConverter.toJson(this, jsonObj);
        return jsonObj;
    }

    public static Authority from(AuthorityType type) {
        return new Authority(type.id, type.title, type.description);
    }

    public static Authority from(long id) {
        return from(AuthorityType.valueOf(id));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}


