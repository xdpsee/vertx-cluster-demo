package pri.zhenhui.demo.tracer.server;

import io.netty.channel.ChannelPipeline;
import pri.zhenhui.demo.tracer.domain.Configs;

public interface ServerConnector {

    Context context();

    Configs configs();

    Protocol protocol();

    void initPipeline(ChannelPipeline pipeline);

}


