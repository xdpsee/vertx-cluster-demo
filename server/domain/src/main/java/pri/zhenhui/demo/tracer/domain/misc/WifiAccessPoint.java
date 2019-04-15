package pri.zhenhui.demo.tracer.domain.misc;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.io.Serializable;

@DataObject(generateConverter = true)
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

    public WifiAccessPoint() {}

    public WifiAccessPoint(JsonObject jsonObj) {
        WifiAccessPointConverter.fromJson(jsonObj, this);
    }

    public JsonObject toJson() {
        JsonObject jsonObj = new JsonObject();
        WifiAccessPointConverter.toJson(this, jsonObj);
        return jsonObj;
    }

    // Getter,Setter

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public Integer getSignalStrength() {
        return signalStrength;
    }

    public void setSignalStrength(Integer signalStrength) {
        this.signalStrength = signalStrength;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }
}
