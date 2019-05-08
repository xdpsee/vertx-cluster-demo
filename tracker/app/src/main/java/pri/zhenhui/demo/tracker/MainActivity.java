package pri.zhenhui.demo.tracker;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;

import pri.zhenhui.demo.tracker.location.LocationService;
import pri.zhenhui.demo.tracker.permission.PermissionStatusGroup;
import pri.zhenhui.demo.tracker.setting.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    private LocationService locationService;

    private final PermissionStatusGroup mPermissionStatusGroup
            = new PermissionStatusGroup(new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    });

    private static final int PERMISSION_REQUEST_CODE = 9527;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.root_content, new SettingsFragment())
                .commit();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        final Intent intent = new Intent(this.getApplication(), LocationService.class);
        getApplicationContext().startService(intent);
        getApplicationContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (PERMISSION_REQUEST_CODE == requestCode) {
            mPermissionStatusGroup.updateStatus(permissions, grantResults);
            if (mPermissionStatusGroup.checkPermissions(MainActivity.this)) {
                locationService.startTracking();
            }
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            String name = className.getClassName();
            if (name.endsWith("LocationService")) {
                locationService = ((LocationService.LocationServiceBinder) service).getService();

                if (!mPermissionStatusGroup.checkPermissions(MainActivity.this)) {
                    mPermissionStatusGroup.requestPermissions(MainActivity.this, PERMISSION_REQUEST_CODE);
                } else {
                    locationService.startTracking();
                }
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            if (className.getClassName().equals("LocationService")) {
                locationService = null;
            }
        }
    };
}
