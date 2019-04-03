package pri.zhenhui.demo.account.domain;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import pri.zhenhui.demo.account.domain.enums.AuthorityType;

import java.io.Serializable;

@SuppressWarnings("unused")
@DataObject(generateConverter = true)
public class Authority implements Serializable {

    private static final long serialVersionUID = 7474327774872249710L;

    private long id;

    private String title;

    private String description;

    public Authority() {}

    public Authority(JsonObject jsonObj) {

    }

    public JsonObject toJson() {
        return null;
    }

    public static Authority from(AuthorityType type) {

        Authority authority = new Authority();
        authority.id = type.id;
        authority.title = type.title;
        authority.description = type.description;

        return authority;
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


