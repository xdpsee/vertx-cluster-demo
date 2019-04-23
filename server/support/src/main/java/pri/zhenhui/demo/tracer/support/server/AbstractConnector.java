package pri.zhenhui.demo.tracer.support.server;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import pri.zhenhui.demo.tracer.domain.Configs;
import pri.zhenhui.demo.tracer.server.Context;
import pri.zhenhui.demo.tracer.server.ServerConnector;

import java.nio.charset.Charset;

public abstract class AbstractConnector implements ServerConnector {

    private static final Logger logger = LoggerFactory.getLogger(AbstractConnector.class);

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

        try {
            final String path = configFilePath();
            String json = context.fileSystem().readFileBlocking(path).toString(Charset.forName("UTF-8"));

            try {
                return new Configs(json);
            } finally {
                logger.info("loading config file: " + path + "\r\n" + json);
            }

        } catch (Throwable e) {
            throw new IllegalStateException("Load config file: " + configFilePath() + " failed!", e);
        }

    }

    private String configFilePath() {
        return String.format("conf/%s.json", protocol().getName());
    }
}
