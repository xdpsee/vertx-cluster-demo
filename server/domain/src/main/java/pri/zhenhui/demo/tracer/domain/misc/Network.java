package pri.zhenhui.demo.tracer.domain.misc;


import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@DataObject(generateConverter = true, inheritConverter = true)
public class Network implements Serializable {

    private static final long serialVersionUID = 6463361247973962010L;

    public Network() { }

    public Network(CellTower cellTower) {
        cellTowers.add(cellTower);
    }

    public Network(JsonObject jsonObj) {
        NetworkConverter.fromJson(jsonObj, this);
    }

    public JsonObject toJson() {
        JsonObject jsonObj = new JsonObject();
        NetworkConverter.toJson(this, jsonObj);
        return jsonObj;
    }

    // 属性
    private Integer mobileCountryCode;

    private Integer mobileNetworkCode;

    private String radioType = "gsm";

    private String carrier;

    private Boolean considerIp = false;

    private List<CellTower> cellTowers = new ArrayList<>();

    private List<WifiAccessPoint> wifiAccessPoints = new ArrayList<>();

    // Getter,Setter
    public Integer getMobileCountryCode() {
        return mobileCountryCode;
    }

    public void setMobileCountryCode(Integer mobileCountryCode) {
        this.mobileCountryCode = mobileCountryCode;
    }

    public Integer getMobileNetworkCode() {
        return mobileNetworkCode;
    }

    public void setMobileNetworkCode(Integer mobileNetworkCode) {
        this.mobileNetworkCode = mobileNetworkCode;
    }

    public String getRadioType() {
        return radioType;
    }

    public void setRadioType(String radioType) {
        this.radioType = radioType;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public Boolean getConsiderIp() {
        return considerIp;
    }

    public void setConsiderIp(Boolean considerIp) {
        this.considerIp = considerIp;
    }

    public List<CellTower> getCellTowers() {
        return cellTowers;
    }

    public List<WifiAccessPoint> getWifiAccessPoints() {
        return wifiAccessPoints;
    }

    public void setCellTowers(List<CellTower> cellTowers) {
        this.cellTowers = cellTowers;
    }

    public void setWifiAccessPoints(List<WifiAccessPoint> wifiAccessPoints) {
        this.wifiAccessPoints = wifiAccessPoints;
    }
}
