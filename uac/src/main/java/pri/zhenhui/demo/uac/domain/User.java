package pri.zhenhui.demo.uac.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import org.apache.commons.beanutils.BeanUtils;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("unused")
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

