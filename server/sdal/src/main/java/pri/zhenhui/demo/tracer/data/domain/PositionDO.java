package pri.zhenhui.demo.tracer.data.domain;

import lombok.Data;
import pri.zhenhui.demo.tracer.domain.UniqueID;
import pri.zhenhui.demo.tracer.domain.misc.Network;

import java.io.Serializable;
import java.util.Date;

@Data
public class PositionDO extends BaseDO implements Serializable {

    private static final long serialVersionUID = -3141351595284439885L;

    private Long id;

    private UniqueID deviceId;

    private Date time;

    private boolean located;

    private double latitude;

    private double longitude;

    private double altitude;

    private double speed; // value in knots

    private double course;

    private double accuracy;

    private Network network;

}
