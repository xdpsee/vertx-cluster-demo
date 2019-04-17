package pri.zhenhui.demo.tracer.data.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@SuppressWarnings("unchecked,unused")
public class AbstractCache<K extends Serializable, V extends Serializable> {

    private final String cacheName;

    private CacheManager cacheManager;

    private Cache cache;

    public AbstractCache(String cacheName) {
        this.cacheName = cacheName;
    }

    @PostConstruct
    private void init() {
        this.cacheManager = CacheManager.newInstance("ehcache.xml");
        this.cache = this.cacheManager.getCache(this.cacheName);
    }

    public V get(K key) {

        final Element element = cache.get(key);
        if (element != null && !element.isExpired()) {
            return (V) element.getObjectValue();
        }

        return null;
    }

    public Map<K, V> multiGet(Set<K> keys) {

        final Map<K, V> result = new HashMap<>();

        if (CollectionUtils.isEmpty(keys)) {
            return result;
        }

        Map<Object, Element> elementMap = cache.getAll(keys);
        result.putAll(elementMap.values()
                .stream()
                .filter(e -> e != null && !e.isExpired())
                .collect(toMap(e -> (K) e.getObjectKey(), e -> (V) e.getObjectValue()))
        );

        return result;
    }

    public void put(K key, V value) {
        if (null == key || null == value) {
            throw new IllegalArgumentException("null == key || null == value");
        }

        Element element = new Element(key, value);
        cache.put(element);
    }

    public void multiPut(Map<K, V> elements) {

        if (!MapUtils.isEmpty(elements)) {
            cache.putAll(elements.entrySet()
                    .stream()
                    .map(e -> new Element(e.getKey(), e.getValue()))
                    .collect(Collectors.toList())
            );
        }
    }

    public void evict(K key) {
        cache.remove(key);
    }

}


