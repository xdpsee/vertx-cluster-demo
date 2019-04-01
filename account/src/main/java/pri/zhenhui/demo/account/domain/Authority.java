package pri.zhenhui.demo.account.domain;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("unused")
@DataObject(generateConverter = true)
public class Authority implements Serializable {

    private static final long serialVersionUID = 6727188224701127966L;

    private Long id;

    private String title;

    private String description;

    private Date createAt;

    private Date updateAt;

    public Authority() {}

    public Authority(JsonObject jsonObj) {
        AuthorityConverter.fromJson(jsonObj, this);
    }

    public JsonObject toJson() {
        JsonObject jsonObj = new JsonObject();
        AuthorityConverter.toJson(this, jsonObj);
        return jsonObj;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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


