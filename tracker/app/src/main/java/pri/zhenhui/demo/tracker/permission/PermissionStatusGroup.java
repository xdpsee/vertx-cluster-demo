package pri.zhenhui.demo.tracker.permission;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PermissionStatusGroup {

    private final List<PermissionStatus> permissionStatuses = new ArrayList<>();

    public PermissionStatusGroup(String[] permissions) {
        this.permissionStatuses.addAll(Arrays.stream(permissions)
                .map(PermissionStatus::new)
                .collect(Collectors.toList())
        );
    }

    public boolean checkPermissions(Context context) {

        permissionStatuses.forEach(e -> e.setStatus(ActivityCompat.checkSelfPermission(context, e.permission)));

        return permissionStatuses.stream().allMatch(PermissionStatus::isGranted);
    }

    public void requestPermissions(Activity activity, int requestCode) {

        List<PermissionStatus> needRequest = permissionStatuses.stream()
                .filter(e -> !e.isGranted())
                .collect(Collectors.toList());

        if (!needRequest.isEmpty()) {
            ActivityCompat.requestPermissions(activity
                    , needRequest.stream().map(e -> e.permission).toArray(String[]::new)
                    , requestCode);
        }
    }

    public void updateStatus(String[] permissions, int[] statuses) {

        for (int i = 0; i < permissions.length; ++i) {
            String permission = permissions[i];
            int status = statuses[i];
            permissionStatuses.stream()
                    .filter(e -> e.permission.equals(permission))
                    .findAny()
                    .ifPresent(ps -> {
                ps.setStatus(status);
            });
        }

    }
}

