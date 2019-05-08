package pri.zhenhui.demo.tracker.permission;

public class PermissionStatus {

    final String permission;

    private int status = -1;

    PermissionStatus(String permission) {
        this.permission = permission;
    }

    void setStatus(int status) {
        this.status = status;
    }

    boolean isGranted() {
        return status == 0;
    }

}

