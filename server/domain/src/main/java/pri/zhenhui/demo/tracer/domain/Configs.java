package pri.zhenhui.demo.tracer.domain;

import io.vertx.core.json.JsonObject;

public class Configs extends JsonObject {

    public static String EVENT_OVERSPEED_NOT_REPEAT = "event.overspeed.notRepeat";
    public static String EVENT_MOTION_SPEED_THRESHOLD = "event.motion.speedThreshold";


    
    public Configs() {
        super();
    }

    public Configs(String json) {
        super(json);
    }

}
