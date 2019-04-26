package pri.zhenhui.demo.tracer.mobile.codec;

import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang3.StringUtils;
import pri.zhenhui.demo.tracer.domain.Message;
import pri.zhenhui.demo.tracer.domain.Position;
import pri.zhenhui.demo.tracer.domain.UniqueID;
import pri.zhenhui.demo.tracer.domain.UniqueType;
import pri.zhenhui.demo.tracer.exception.DecodecException;
import pri.zhenhui.demo.tracer.idgen.PositionIdGen;
import pri.zhenhui.demo.tracer.mobile.message.RegistryMessage;
import pri.zhenhui.demo.tracer.support.codec.AbstractProtocolDecoder;

import java.util.*;

import static pri.zhenhui.demo.tracer.domain.Position.KEY_ALARM;

public class MobileProtocolDecoder extends AbstractProtocolDecoder<String, Message> {

    @Override
    public List<Message> decode(ChannelHandlerContext ctx, String message) throws Exception {

        try {
            if (message.startsWith("##1,")) {
                return Collections.singletonList(decodeRegistry(message));
            }

            if (message.startsWith("##2,")) {
                return Collections.singletonList(decodePosition(message));
            }

            return new ArrayList<>();
        } catch (Throwable e) {
            throw new DecodecException(String.format("decode message: %s exception", message), e);
        }
    }

    /**
     * ##1,IMEI#
     * @param message frame message
     * @return RegistryMessage
     */
    public RegistryMessage decodeRegistry(String message) {

        String[] components = message.split("[#|,]");
        String imei = components[3];

        return new RegistryMessage(message, new UniqueID(UniqueType.IMEI, imei));

    }

    /**
     * ##2,IMEI,latitude,longitude,altitude,course,speed,time,outdated,alarms=[sos|low_battery]#
     * @param message frame message
     * @return Position
     */
    private Position decodePosition(String message) throws DecodecException{

        Position position = new Position();

        String[] components = message.split("[#,]");
        if (components.length != 11 && components.length != 12) {
            throw new DecodecException("invalid message: " + message);
        }

        position.setId(PositionIdGen.next());
        position.setDeviceId(new UniqueID(UniqueType.IMEI, components[3]));
        position.setLocated(true);
        position.setLatitude(Double.parseDouble(components[4]));
        position.setLongitude(Double.parseDouble(components[5]));
        position.setAltitude(Double.parseDouble(components[6]));
        position.setCourse(Double.parseDouble(components[7]));
        position.setSpeed(Double.parseDouble(components[8]));
        position.setTime(new Date(Long.parseLong(components[9])));
        position.set(Position.KEY_OUTDATED, "1".equals(components[10]));

        if (components.length == 12) {
            final String alarms = components[11];
            if (StringUtils.isNotBlank(alarms)) {
                final List<String> alerts = new ArrayList<>();
                Arrays.stream(alarms.split("\\|")).forEach(alarm -> {
                    if ("sos".equalsIgnoreCase(alarm)) {
                        alerts.add(Position.ALARM_SOS);
                    }
                    if ("low_battery".equalsIgnoreCase(alarm)) {
                        alerts.add(Position.ALARM_LOW_BATTERY);
                    }
                });

                if (!alerts.isEmpty()) {
                    position.set(KEY_ALARM, StringUtils.join(alerts, ","));
                }
            }
        }

        return position;
    }
}






