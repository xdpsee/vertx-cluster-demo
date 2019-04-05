package pri.zhenhui.demo.account.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("unused")
@Data
@DataObject
public class User implements Serializable {

    private static final long serialVersionUID = -5781360306973268721L;

    private Long id;

    private String username;

    private String password;

    private String avatar;

    private String phone;

    private String email;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date createAt;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date updateAt;

    public User() {}

    public User(JsonObject jsonObj) {
        User r = Json.decodeValue(jsonObj.toString(), User.class);
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
}

