package pri.zhenhui.demo.tracer.support.utils;

import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class GMT {


    /**
     *
     * @param year eg. 2017
     * @param month  1...12
     * @param day 1...
     * @param hourOfDay 0-23
     * @param minute 0...60
     * @param second 0...59
     * @return date in gmt timezone
     */
    public static Date date(int year, int month, int day
            , int hourOfDay, int minute, int second) {

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("GMT")));
        calendar.set(year, month-1, day, hourOfDay, minute, second);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

}
