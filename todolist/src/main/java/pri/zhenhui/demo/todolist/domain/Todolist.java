package pri.zhenhui.demo.todolist.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import org.apache.commons.beanutils.BeanUtils;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("unused")
@DataObject
public class Todolist implements Serializable {

    private static final long serialVersionUID = -8154454363081002517L;

    public static final String STATUS_TODO = "TODO";

    public static final String STATUS_PROCESSING = "PROCESSING";

    public static final String STATUS_FINISHED = "FINISHED";

    public static final String STATUS_OBSOLETED = "OBSOLETED";

    public static final String STATUS_DELETED = "DELETED";

    private String id;

    private String title;

    private String status;

    private Long userId;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date createAt;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date updateAt;

    public Todolist() {
    }

    public Todolist(JsonObject jsonObj) {
        Todolist r = Json.decodeValue(jsonObj.toString(), Todolist.class);
        try {
            BeanUtils.copyProperties(this, r);
        } catch (Exception e) {
            throw new IllegalStateException("todolist copy properties");
        }
    }

    public JsonObject toJson() {
        String json = Json.encode(this);
        return new JsonObject(json);
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }
}

