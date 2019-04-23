package pri.zhenhui.demo.webapi.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pri.zhenhui.demo.support.utils.ExceptionUtils;

import java.io.Serializable;

@SuppressWarnings("unused")
public final class Result<T> implements Serializable {

    private static final long serialVersionUID = -5906925430812625539L;

    private static final Logger logger = LoggerFactory.getLogger(Result.class);

    private int error;

    private String message;

    private T data;

    private String debug;

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.message = "success";
        result.data = data;
        return result;
    }

    public static <T> Result<T> error(int error, String message) {
        Result<T> result = new Result<>();
        result.error = error;
        result.message = message;
        return result;
    }

    public static <T> Result<T> error(int error, String message, Throwable throwable) {
        Result<T> result = new Result<>();
        result.error = error;
        result.message = message;
        if (logger.isDebugEnabled()) {
            result.debug = ExceptionUtils.getStackTrace(throwable);
        }

        return result;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getDebug() {
        return debug;
    }

    public void setDebug(String debug) {
        this.debug = debug;
    }

    private Result() {}

}
