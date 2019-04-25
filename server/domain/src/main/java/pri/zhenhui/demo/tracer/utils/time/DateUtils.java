package pri.zhenhui.demo.tracer.utils.time;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    /**
     * 仅处理格式 yyyy-MM-dd'T'HH:mm:ss.SSSZ
     */
    private static final ThreadLocal<DateFormat> dateFormat
            = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));

    public static Date parse(String date) {

        try {
            return dateFormat.get().parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String format(Date date) {
        if (date != null) {
            return dateFormat.get().format(date);
        }

        return null;
    }
}
