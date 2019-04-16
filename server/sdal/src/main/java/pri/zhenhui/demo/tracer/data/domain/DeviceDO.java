package pri.zhenhui.demo.tracer.data.domain;

import lombok.Data;
import pri.zhenhui.demo.tracer.domain.UniqueID;
import pri.zhenhui.demo.tracer.enums.DeviceStatus;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public class DeviceDO extends BaseDO implements Serializable {

    private static final long serialVersionUID = -8449881351107661349L;

    private UniqueID id;

    private String model;

    private String protocol;

    private Map<String, Object> attributes = new HashMap<>();

    private DeviceStatus status = DeviceStatus.NORMAL;

}





