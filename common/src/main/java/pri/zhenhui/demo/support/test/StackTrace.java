package pri.zhenhui.demo.support.test;

import io.vertx.core.AsyncResult;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public final class StackTrace {

    private static final Logger logger = LoggerFactory.getLogger(StackTrace.class);

    public static <T> void printIfErr(AsyncResult<T> result) {

        if (logger.isDebugEnabled() && result.failed()) {
            result.cause().printStackTrace();
        }
    }

}

