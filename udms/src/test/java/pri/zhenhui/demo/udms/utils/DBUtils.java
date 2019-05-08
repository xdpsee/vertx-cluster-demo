package pri.zhenhui.demo.udms.utils;

import io.vertx.core.Vertx;

public final class DBUtils {

    public static synchronized void clearDB(Vertx vertx) {

        String path = System.getProperty("user.home") + "/db/account.mv.db";
        if (vertx.fileSystem().existsBlocking(path)) {
            vertx.fileSystem().deleteBlocking(path);
        }

        path = System.getProperty("user.home") + "/db/account.trace.db";
        if (vertx.fileSystem().existsBlocking(path)) {
            vertx.fileSystem().deleteBlocking(path);
        }

    }

}
