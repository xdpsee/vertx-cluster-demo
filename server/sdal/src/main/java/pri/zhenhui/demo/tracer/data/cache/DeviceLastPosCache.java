package pri.zhenhui.demo.tracer.data.cache;

import pri.zhenhui.demo.support.cache.AbstractEhcache;
import pri.zhenhui.demo.tracer.domain.Position;
import pri.zhenhui.demo.tracer.domain.UniqueID;

public class DeviceLastPosCache extends AbstractEhcache<UniqueID, Position> {

    public DeviceLastPosCache() {
        super(CacheName.DEVICE_LAST_POSITION_CACHE);
    }
}

