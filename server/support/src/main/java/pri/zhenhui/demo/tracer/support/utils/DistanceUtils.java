package pri.zhenhui.demo.tracer.support.utils;

public final class DistanceUtils {

    private DistanceUtils() {
    }

    private static final double EQUATORIAL_EARTH_RADIUS = 6378.1370;
    private static final double DEG_TO_RAD = Math.PI / 180;

    public static double distance(double lat1, double lng1, double lat2, double lng2) {
        double lngd = (lng2 - lng1) * DEG_TO_RAD;
        double latd = (lat2 - lat1) * DEG_TO_RAD;
        double a = Math.pow(Math.sin(latd / 2), 2)
                + Math.cos(lat1 * DEG_TO_RAD) * Math.cos(lat2 * DEG_TO_RAD) * Math.pow(Math.sin(lngd / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = EQUATORIAL_EARTH_RADIUS * c;
        return d * 1000;
    }

    public static double distanceToLine(
            double pointLat, double pointLon, double lat1, double lon1, double lat2, double lon2) {
        double d0 = distance(pointLat, pointLon, lat1, lon1);
        double d1 = distance(lat1, lon1, lat2, lon2);
        double d2 = distance(lat2, lon2, pointLat, pointLon);
        if (Math.pow(d0, 2) > Math.pow(d1, 2) + Math.pow(d2, 2)) {
            return d2;
        }
        if (Math.pow(d2, 2) > Math.pow(d1, 2) + Math.pow(d0, 2)) {
            return d0;
        }
        double halfP = (d0 + d1 + d2) * 0.5;
        double area = Math.sqrt(halfP * (halfP - d0) * (halfP - d1) * (halfP - d2));
        return 2 * area / d1;
    }

}
