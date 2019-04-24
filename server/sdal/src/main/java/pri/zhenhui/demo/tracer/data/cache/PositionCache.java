package pri.zhenhui.demo.tracer.data.cache;

import pri.zhenhui.demo.support.cache.AbstractEhcache;
import pri.zhenhui.demo.tracer.domain.Position;

public class PositionCache extends AbstractEhcache<Long, Position> {

    public PositionCache() {
        super(CacheName.POSITION_CACHE);
    }

}
