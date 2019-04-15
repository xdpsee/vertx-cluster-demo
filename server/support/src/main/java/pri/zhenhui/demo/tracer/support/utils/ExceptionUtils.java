package pri.zhenhui.demo.tracer.support.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class ExceptionUtils {

    public static String getStackTrace(Throwable aThrowable) {
        if (null != aThrowable) {
            final Writer result = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(result);
            aThrowable.printStackTrace(printWriter);
            return result.toString().replaceAll("\\n", "-");
        }

        return "";
    }

}
