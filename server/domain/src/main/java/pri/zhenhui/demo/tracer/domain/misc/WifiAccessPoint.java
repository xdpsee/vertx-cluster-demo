package pri.zhenhui.demo.tracer.domain.misc;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import lombok.Data;
import lombok.NoArgsConstructor;
import pri.zhenhui.demo.tracer.utils.JsonUtils;

import java.io.Serializable;

@DataObject
@Data
@NoArgsConstructor
public class WifiAccessPoint implements Serializable {

    private static final long serialVersionUID = 436184760096180703L;

    private String macAddress;

    private Integer signalStrength;

    private Integer channel;

    public static WifiAccessPoint from(String macAddress, int signalStrength) {
        WifiAccessPoint wifiAccessPoint = new WifiAccessPoint();
        wifiAccessPoint.setMacAddress(macAddress);
        wifiAccessPoint.setSignalStrength(signalStrength);
        return wifiAccessPoint;
    }

    public static WifiAccessPoint from(String macAddress, int signalStrength, int channel) {
        WifiAccessPoint wifiAccessPoint = from(macAddress, signalStrength);
        wifiAccessPoint.setChannel(channel);
        return wifiAccessPoint;
    }

    public WifiAccessPoint(JsonObject jsonObj) {
        JsonUtils.fromJson(jsonObj, this);
    }

    public JsonObject toJson() {
        return JsonUtils.toJson(this);
    }
}
