package pri.zhenhui.demo.tracker.location;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import java.util.Date;
import java.util.List;

import pri.zhenhui.demo.tracker.data.DBUtils;
import pri.zhenhui.demo.tracker.data.Position;

class LocationListener implements android.location.LocationListener {

    private final static String TAG = "LocationListener";

    private final Context context;

    private Location mLastLocation;

    public LocationListener(String provider, Context context) {
        this.context = context;
        this.mLastLocation = new Location(provider);
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        Log.i(TAG, "LocationChanged: " + location);

        Position position = new Position();
        position.setTime(new Date(location.getTime()));
        position.setLatitude(location.getLatitude());
        position.setLongitude(location.getLongitude());
        position.setAltitude(location.getAltitude());
        position.setCourse(location.getBearing());
        position.setAccuracy(location.getAccuracy());
        position.setSpeed(location.getSpeed());
        position.setAlerts("no");

        DBUtils.getPositionDao().save(position);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.e(TAG, "onProviderDisabled: " + provider);
    }
    
    @Override
    public void onProviderEnabled(String provider) {
        Log.e(TAG, "onProviderEnabled: " + provider);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.e(TAG, "onStatusChanged: " + status);

        List<Position> positions = DBUtils.getPositionDao().queryBuilder().list();
        Log.d(TAG, "positions.size = " + positions.size());

    }
}
