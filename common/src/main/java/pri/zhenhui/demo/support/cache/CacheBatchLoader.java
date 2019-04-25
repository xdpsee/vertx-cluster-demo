package pri.zhenhui.demo.support.cache;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

@FunctionalInterface
public interface CacheBatchLoader<K extends Serializable, V extends Serializable> {

    Map<K, V> call(Set<K> keys);

}
