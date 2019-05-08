package pri.zhenhui.demo.udms.dal.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleAuthorityBindDO {

    @NonNull
    private Long roleId;

    @NonNull
    private Long authorityId;

}


