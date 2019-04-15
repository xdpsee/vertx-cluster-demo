package pri.zhenhui.demo.tracer.support.handler.filter;


import pri.zhenhui.demo.tracer.server.ServerConnector;

public abstract class AbstractFilterPolicy implements FilterPolicy {

    protected final ServerConnector connector;

    public AbstractFilterPolicy(ServerConnector connector) {
        this.connector = connector;
    }
}


