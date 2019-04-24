package pri.zhenhui.demo.uac.utils;

import io.vertx.core.AsyncResult;

public final class StackTrace {

    public static <T> void print(AsyncResult<T> result) {

        if (result.failed()) {
            result.cause().printStackTrace();
        }
    }

}
