package pri.zhenhui.demo.support.cache;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public interface Cache<K extends Serializable, V extends Serializable> {

    V get(K key);

    V get(K key, CacheLoader<V> loader) throws Exception;

    void put(K key, V value);

    Map<K, V> multiGet(Set<K> keys);

    Map<K, V> multiGet(Set<K> keys, CacheBatchLoader<K, V> loader);

    void multiPut(Map<K, V> elements);

    void evict(K key);

}

