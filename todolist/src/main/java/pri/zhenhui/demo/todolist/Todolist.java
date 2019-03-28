package pri.zhenhui.demo.todolist;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.io.Serializable;

@SuppressWarnings("unused")
@DataObject(generateConverter = true)
public class Todolist implements Serializable {

    private static final long serialVersionUID = -8154454363081002517L;

    public static final String STATUS_TODO = "TODO";

    public static final String STATUS_PROCESSING = "PROCESSING";

    public static final String STATUS_FINISHED = "FINISHED";

    public static final String STATUS_DELETED = "DELETED";

    private String id;

    private String title;

    private String status;

    public Todolist(JsonObject jsonObj) {
        TodolistConverter.fromJson(jsonObj, this);
    }

    public JsonObject toJson() {
        JsonObject jsonObj = new JsonObject();
        TodolistConverter.toJson(this, jsonObj);
        return jsonObj;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

