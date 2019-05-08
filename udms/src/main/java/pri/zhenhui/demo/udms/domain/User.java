package pri.zhenhui.demo.udms.domain;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.beanutils.BeanUtils;
import pri.zhenhui.demo.udms.dal.domain.UserDO;

import java.io.Serializable;

@SuppressWarnings("unused")
@Data
@NoArgsConstructor
@DataObject(generateConverter = true)
public class User implements Serializable {

    private static final long serialVersionUID = -5781360306973268721L;

    private Long id;

    private long parentId;

    private String username;

    private String password;

    private String avatar;

    private String phone;

    private String email;

    public User(UserDO userDO) {
        try {
            BeanUtils.copyProperties(this, userDO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public User(JsonObject jsonObj) {
        UserConverter.fromJson(jsonObj, this);
    }

    public JsonObject toJson() {
        JsonObject jsonObj = new JsonObject();
        UserConverter.toJson(this, jsonObj);
        return jsonObj;
    }
}

