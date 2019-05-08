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
public class DeviceGroupDO extends BaseDO implements Serializable {

    private static final long serialVersionUID = -1812730316974573052L;

    private Long id;
    @NonNull
    private Long userId;
    @NonNull
    private String title;

}
