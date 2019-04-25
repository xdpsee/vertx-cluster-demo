package pri.zhenhui.demo.support.cache;

import java.io.Serializable;

@FunctionalInterface
public interface CacheLoader<V extends Serializable> {

    V call();

}

