package pri.zhenhui.demo.tracer.server;

import pri.zhenhui.demo.tracer.domain.UniqueID;
import pri.zhenhui.demo.tracer.server.Connection;

public interface ConnectionManager {

    void register(Connection connection);

    Connection lookup(UniqueID deviceId);

    void closeAll();
}

