package pri.zhenhui.demo.tracer.mobile.codec;

import pri.zhenhui.demo.tracer.domain.Command;
import pri.zhenhui.demo.tracer.domain.UniqueID;
import pri.zhenhui.demo.tracer.support.codec.AbstractProtocolEncoder;

public class MobileProtocolEncoder extends AbstractProtocolEncoder {

    @Override
    public byte[] encodeCommand(Command command) throws Exception {
        return new byte[0];
    }

    @Override
    public String encodeDeviceId(UniqueID deviceId) {
        return null;
    }
}


