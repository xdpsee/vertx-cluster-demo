package pri.zhenhui.demo.tracer.data.cache;

import pri.zhenhui.demo.support.cache.AbstractEhcache;
import pri.zhenhui.demo.tracer.domain.Device;
import pri.zhenhui.demo.tracer.domain.UniqueID;

public class DeviceCache extends AbstractEhcache<UniqueID, Device> {

    public DeviceCache() {
        super(CacheName.DEVICE_CACHE);
    }

}

