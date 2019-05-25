package pri.zhenhui.demo.udms.service.exception;

import io.vertx.serviceproxy.ServiceException;

/**
 * @author zhenhui
 */
public enum Errors {
    SYSTEM_EXCEPTION(-1, "系统异常"),

    USER_NOT_FOUND(1, "用户不存在"),
    USER_PASSWORD_MISMATCH(2, "密码错误"),
    ;

    public final int code;
    public final String message;

    Errors(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ServiceException exception() {
        return new ServiceException(code, message);
    }
}

