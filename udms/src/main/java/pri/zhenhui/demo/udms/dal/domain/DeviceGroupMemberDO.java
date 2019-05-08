package pri.zhenhui.demo.udms.dal.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import pri.zhenhui.demo.tracer.data.domain.BaseDO;
import pri.zhenhui.demo.tracer.domain.UniqueID;

import java.io.Serializable;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class DeviceGroupMemberDO extends BaseDO implements Serializable {

    private static final long serialVersionUID = 2185143364469954084L;

    @NonNull
    private Long groupId;
    @NonNull
    private UniqueID deviceId;

}
