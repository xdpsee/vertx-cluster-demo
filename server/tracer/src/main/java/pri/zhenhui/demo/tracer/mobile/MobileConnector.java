package pri.zhenhui.demo.tracer.mobile;

import pri.zhenhui.demo.tracer.server.Encoder;
import pri.zhenhui.demo.tracer.server.Processor;
import pri.zhenhui.demo.tracer.server.Protocol;
import pri.zhenhui.demo.tracer.support.server.AbstractConnector;
import pri.zhenhui.demo.tracer.support.server.ServerContext;

import java.util.List;

public class MobileConnector extends AbstractConnector {

    public MobileConnector(ServerContext context) {
        super(context);
    }

    @Override
    public Protocol protocol() {
        return new MobileProtocol(context);
    }

    @Override
    public List<Processor> processors() {
        return null;
    }

    @Override
    public Encoder encoder() {
        return null;
    }
}
