package pri.zhenhui.demo.tracer.server;

import pri.zhenhui.demo.tracer.domain.Configs;

import java.util.List;

public interface ServerConnector {

    Context context();

    Configs configs();

    Protocol protocol();

    List<Processor> processors();

    Encoder encoder();

}


