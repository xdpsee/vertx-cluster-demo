package pri.zhenhui.demo.tracer.server;

import pri.zhenhui.demo.tracer.domain.Command;
import pri.zhenhui.demo.tracer.domain.UniqueID;

public interface Encoder {

    byte[] encodeCommand(Command command) throws Exception;

    String encodeDeviceId(UniqueID deviceId);
}


