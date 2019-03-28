package pri.zhenhui.demo.todolist;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Converter for {@link pri.zhenhui.demo.todolist.Todolist}.
 * NOTE: This class has been automatically generated from the {@link pri.zhenhui.demo.todolist.Todolist} original class using Vert.x codegen.
 */
public class TodolistConverter {

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, Todolist obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "id":
          if (member.getValue() instanceof String) {
            obj.setId((String)member.getValue());
          }
          break;
        case "status":
          if (member.getValue() instanceof String) {
            obj.setStatus((String)member.getValue());
          }
          break;
        case "title":
          if (member.getValue() instanceof String) {
            obj.setTitle((String)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(Todolist obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(Todolist obj, java.util.Map<String, Object> json) {
    if (obj.getId() != null) {
      json.put("id", obj.getId());
    }
    if (obj.getStatus() != null) {
      json.put("status", obj.getStatus());
    }
    if (obj.getTitle() != null) {
      json.put("title", obj.getTitle());
    }
  }
}
