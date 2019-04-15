package pri.zhenhui.demo.tracer.domain;

@SuppressWarnings("unused")
public enum UniqueType {
    IMEI(1, "IMEI");

    public final int code;
    public final String comment;
    UniqueType(int code, String comment) {
        this.code = code;
        this.comment = comment;
    }

}
