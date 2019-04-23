package pri.zhenhui.demo.tracer.data.cache;

import pri.zhenhui.demo.support.cache.AbstractCache;
import pri.zhenhui.demo.tracer.domain.Position;

public class PositionCache extends AbstractCache<Long, Position> {

    public PositionCache() {
        super(CacheName.POSITION_CACHE);
    }

}
