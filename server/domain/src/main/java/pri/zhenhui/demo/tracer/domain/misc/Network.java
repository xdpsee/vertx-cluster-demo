package pri.zhenhui.demo.tracer.domain.misc;


import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import lombok.Data;
import lombok.NoArgsConstructor;
import pri.zhenhui.demo.tracer.utils.JsonUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@DataObject
@Data
@NoArgsConstructor
public class Network implements Serializable {

    private static final long serialVersionUID = 6463361247973962010L;

    public Network(CellTower cellTower) {
        cellTowers.add(cellTower);
    }

    public Network(JsonObject jsonObj) {
        JsonUtils.fromJson(jsonObj, this);
    }

    public JsonObject toJson() {
        return JsonUtils.toJson(this);
    }

    // 属性
    private Integer mobileCountryCode;

    private Integer mobileNetworkCode;

    private String radioType = "gsm";

    private String carrier;

    private Boolean considerIp = false;

    private List<CellTower> cellTowers = new ArrayList<>();

    private List<WifiAccessPoint> wifiAccessPoints = new ArrayList<>();
}
