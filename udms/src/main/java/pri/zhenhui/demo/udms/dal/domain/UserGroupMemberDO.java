package pri.zhenhui.demo.udms.dal.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import pri.zhenhui.demo.tracer.data.domain.BaseDO;

import java.io.Serializable;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class UserGroupMemberDO extends BaseDO implements Serializable {

    private static final long serialVersionUID = -4832890109758504377L;

    @NonNull
    private Long groupId;

    @NonNull
    private Long userId;

}
