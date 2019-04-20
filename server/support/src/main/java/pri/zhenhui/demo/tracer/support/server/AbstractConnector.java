package pri.zhenhui.demo.tracer.support.server;

import io.vertx.core.json.JsonObject;
import org.apache.commons.beanutils.BeanUtils;
import pri.zhenhui.demo.tracer.domain.Configs;
import pri.zhenhui.demo.tracer.server.Context;
import pri.zhenhui.demo.tracer.server.ServerConnector;

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

        try {
            JsonObject jsonObj = context.fileSystem().readFileBlocking(configFilePath()).toJsonObject();
            BeanUtils.copyProperties(configs, jsonObj);
        } catch (Throwable e) {
            throw new IllegalStateException("Load config file: " + configFilePath() + " failed!", e);
        }

        return configs;
    }

    private String configFilePath() {
        return String.format("conf/%s.json", protocol().getName());
    }
}
