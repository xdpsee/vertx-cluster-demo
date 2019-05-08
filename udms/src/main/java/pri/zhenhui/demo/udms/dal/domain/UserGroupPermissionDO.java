package pri.zhenhui.demo.udms.dal.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import pri.zhenhui.demo.tracer.data.domain.BaseDO;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserGroupPermissionDO extends BaseDO implements Serializable {

    private static final long serialVersionUID = -4871603107820245057L;

    @NonNull
    private Long groupId;

    @NonNull
    private Long permissionId;

}
