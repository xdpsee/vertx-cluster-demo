package pri.zhenhui.demo.tracer.data.utils;

import io.vertx.core.Vertx;

public final class DBUtils {

    public static void clearDB(Vertx vertx) {

        String path = System.getProperty("user.home") + "/db/tracer.mv.db";
        if (vertx.fileSystem().existsBlocking(path)) {
            vertx.fileSystem().deleteBlocking(path);
        }

        path = System.getProperty("user.home") + "/db/tracer.trace.db";
        if (vertx.fileSystem().existsBlocking(path)) {
            vertx.fileSystem().deleteBlocking(path);
        }

    }

}
