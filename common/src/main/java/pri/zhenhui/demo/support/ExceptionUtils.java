package pri.zhenhui.demo.support;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class ExceptionUtils {

    public static String getStackTrace(Throwable e) {

        if (null != e) {
            final Writer result = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(result);
            e.printStackTrace(printWriter);
            return result.toString().replaceAll("(\\r|\\n)", "-");
        }

        return "";

    }

}
