package pri.zhenhui.demo.tracer.support.server;

import pri.zhenhui.demo.tracer.domain.Configs;
import pri.zhenhui.demo.tracer.server.Context;
import pri.zhenhui.demo.tracer.server.ServerConnector;

import java.io.InputStream;

public abstract class AbstractConnector implements ServerConnector {

    protected final ServerContext context;

    protected final Configs configs;

    protected AbstractConnector(ServerContext context) {
        this.context = context;
        this.configs = loadConfigs();
    }

    @Override
    public Context context() {
        return context;
    }

    @Override
    public Configs configs() {
        return configs;
    }

    private Configs loadConfigs() {

        final Configs configs = new Configs();

        final InputStream inputStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("conf/" + protocol().getName() + ".json");
        if (null == inputStream) {
            throw new IllegalStateException("No config file found for protocol: " + protocol().getName());
        }

        // TODO:

        return configs;
    }
}
