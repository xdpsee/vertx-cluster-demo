package pri.zhenhui.demo.tracer.mobile.message;

import pri.zhenhui.demo.tracer.domain.Message;
import pri.zhenhui.demo.tracer.domain.UniqueID;
import pri.zhenhui.demo.tracer.domain.UniqueType;

import java.nio.charset.Charset;

public final class RegistryMessage implements Message {

    private final String raw;

    private final UniqueID deviceId;

    public RegistryMessage(String body) {
        this.raw = body;
        String[] components = body.split("[#|,]");
        String imei = components[3];
        deviceId = new UniqueID(UniqueType.IMEI, imei);
    }

    @Override
    public UniqueID deviceId() {
        return deviceId;
    }

    @Override
    public byte[] rawBytes() {
        return raw.getBytes(Charset.forName("UTF-8"));
    }
}
