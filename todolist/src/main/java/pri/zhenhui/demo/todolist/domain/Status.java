package pri.zhenhui.demo.todolist.domain;

public interface Status {

    String STATUS_TODO = "TODO";

    String STATUS_PROCESSING = "PROCESSING";

    String STATUS_FINISHED = "FINISHED";

    String STATUS_OBSOLETED = "OBSOLETED";

    String STATUS_DELETED = "DELETED";

    static boolean accept(String status) {
        return STATUS_TODO.equals(status)
                || STATUS_PROCESSING.equals(status)
                || STATUS_FINISHED.equals(status)
                || STATUS_OBSOLETED.equals(status)
                || STATUS_DELETED.equals(status);
    }

}
