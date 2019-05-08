package pri.zhenhui.demo.tracker.location;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import pri.zhenhui.demo.tracker.R;
import pri.zhenhui.demo.tracker.data.DBUtils;

public class LocationService extends Service {

    private static final String TAG = "LocationService";

    private final LocationServiceBinder binder = new LocationServiceBinder();

    private LocationListener mLocationListener;

    private LocationManager mLocationManager;

    private final int LOCATION_INTERVAL = 15000;

    private final int LOCATION_DISTANCE = 10;

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");
        mLocationListener = new LocationListener(LocationManager.GPS_PROVIDER, getApplicationContext());

        DBUtils.initDB(getApplicationContext());

        startForeground(1, getNotification());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        startTracking();
    }

    private void initializeLocationManager() {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext()
                    .getSystemService(Context.LOCATION_SERVICE);
        }
    }

    public void startTracking() {

        initializeLocationManager();

        try {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER
                    , LOCATION_INTERVAL
                    , LOCATION_DISTANCE
                    , mLocationListener);

        } catch (java.lang.SecurityException e) {
            Log.i(TAG, "Fail to request location update, ignore", e);
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "gps provider does not exist " + e.getMessage());
        }

    }

    public void stopTracking() {

        if (mLocationManager != null) {
            try {
                mLocationManager.removeUpdates(mLocationListener);
            } catch (Exception e) {
                Log.i(TAG, "Fail to remove location listeners, ignore", e);
            }
        }
    }

    private Notification getNotification() {

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Notification notification;

        String channelId = "channel_0";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(channelId, "tracer", NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(mChannel);
            notification = new Notification.Builder(this, channelId)
                    .setTicker("Tracer 设备")
                    .setContentTitle("正在后台运行")
                    .setContentText("点击打开设置选项")
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .build();
        } else {
            notification = new NotificationCompat.Builder(getBaseContext(), channelId)
                    .setContentTitle("正在后台运行")
                    .setContentText("点击打开设置选项")
                    .setAutoCancel(false)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .build();
        }

        return notification;
    }

    public class LocationServiceBinder extends Binder {
        public LocationService getService() {
            return LocationService.this;
        }
    }

}