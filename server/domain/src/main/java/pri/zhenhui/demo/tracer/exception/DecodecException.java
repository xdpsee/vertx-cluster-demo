package pri.zhenhui.demo.tracer.exception;

public class DecodecException extends Exception {

    public DecodecException(String message) {
        super(message);
    }

    public DecodecException(String message, Throwable cause) {
        super(message, cause);
    }

    public DecodecException(Throwable cause) {
        super(cause);
    }
}
