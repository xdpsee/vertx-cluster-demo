package pri.zhenhui.demo.udms.dal.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import pri.zhenhui.demo.tracer.data.domain.BaseDO;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class UserGroupDO extends BaseDO {

    private Long id;

    @NonNull
    private Long userId;

    @NonNull
    private String title;

}
