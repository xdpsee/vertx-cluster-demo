package pri.zhenhui.demo.uac.domain;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

@DataObject(generateConverter = true)
public class RoleAuthorityBind {

    private Long roleId;

    private Long authorityId;

    public RoleAuthorityBind() {}

    public RoleAuthorityBind(JsonObject jsonObj) {
        pri.zhenhui.demo.uac.domain.RoleAuthorityBindConverter.fromJson(jsonObj, this);
    }

    public JsonObject toJson() {
        JsonObject jsonObj = new JsonObject();
        pri.zhenhui.demo.uac.domain.RoleAuthorityBindConverter.toJson(this, jsonObj);
        return jsonObj;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Long authorityId) {
        this.authorityId = authorityId;
    }
}
