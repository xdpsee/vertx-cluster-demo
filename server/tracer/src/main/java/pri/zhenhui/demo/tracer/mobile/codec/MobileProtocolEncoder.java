package pri.zhenhui.demo.tracer.mobile.codec;

import pri.zhenhui.demo.tracer.domain.Command;
import pri.zhenhui.demo.tracer.enums.CommandType;
import pri.zhenhui.demo.tracer.support.codec.AbstractProtocolEncoder;

import static pri.zhenhui.demo.tracer.domain.Command.KEY_DATA;

public class MobileProtocolEncoder extends AbstractProtocolEncoder {

    @Override
    public byte[] encode(Command command) throws Exception {
        if (command.getType().equals(CommandType.TYPE_CUSTOM)) {
            String data = command.getString(KEY_DATA);
            return (data + "\r\n").getBytes("UTF-8");
        }

        return new byte[0];
    }

}


