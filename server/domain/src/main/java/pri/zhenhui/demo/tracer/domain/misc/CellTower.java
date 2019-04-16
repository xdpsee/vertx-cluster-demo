package pri.zhenhui.demo.tracer.domain.misc;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import lombok.Data;
import lombok.NoArgsConstructor;
import pri.zhenhui.demo.tracer.utils.JsonUtils;

import java.io.Serializable;

@Data
@NoArgsConstructor
@DataObject
public class CellTower implements Serializable {

    private static final long serialVersionUID = 1758268454928348674L;

    private String radioType;

    private Long cellId;

    private Integer locationAreaCode;

    private Integer mobileCountryCode;

    private Integer mobileNetworkCode;

    private Integer signalStrength;

    public static CellTower from(int mcc, int mnc, int lac, long cid) {
        CellTower cellTower = new CellTower();
        cellTower.setMobileCountryCode(mcc);
        cellTower.setMobileNetworkCode(mnc);
        cellTower.setLocationAreaCode(lac);
        cellTower.setCellId(cid);
        return cellTower;
    }

    public static CellTower from(int mcc, int mnc, int lac, long cid, int rssi) {
        CellTower cellTower = CellTower.from(mcc, mnc, lac, cid);
        cellTower.setSignalStrength(rssi);
        return cellTower;
    }

    public CellTower(JsonObject jsonObj) {
        JsonUtils.fromJson(jsonObj, this);
    }

    public JsonObject toJson() {
        return JsonUtils.toJson(this);
    }


}

