package pri.zhenhui.demo.webapi.exception;

public final class PermissionException extends Exception {
    public PermissionException() {
        super("无权限");
    }
}