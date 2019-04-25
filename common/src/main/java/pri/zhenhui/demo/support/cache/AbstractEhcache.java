package pri.zhenhui.demo.support.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@SuppressWarnings("unchecked,unused")
public abstract class AbstractEhcache<K extends Serializable, V extends Serializable> implements pri.zhenhui.demo.support.cache.Cache<K, V> {

    private final String cacheName;

    private CacheManager cacheManager;

    private Cache cache;

    public AbstractEhcache(String cacheName) {
        this.cacheName = cacheName;
        init();
    }

    private void init() {
        this.cacheManager = CacheManager.newInstance();
        this.cache = this.cacheManager.getCache(this.cacheName);
    }

    @Override
    public V get(K key) {

        final Element element = cache.get(key);
        if (element != null && !element.isExpired()) {
            return (V) element.getObjectValue();
        }

        return null;
    }

    @Override
    public V get(K key, CacheLoader<V> loader) {

        final Element element = cache.get(key);
        if (element != null && !element.isExpired()) {
            return (V) element.getObjectValue();
        }

        V value = loader.call();
        if (value != null) {
            Element e = new Element(key, value);
            cache.put(e);
        }

        return value;
    }

    @Override
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

    @Override
    public Map<K, V> multiGet(Set<K> keys, CacheBatchLoader<K, V> loader) {
        Map<K, V> results = multiGet(keys);

        final Set<K> absentKeys = new HashSet<>(keys);
        absentKeys.removeAll(results.keySet());

        if (!absentKeys.isEmpty()) {
            Map<K, V> absentMap = loader.call(absentKeys);
            results.putAll(absentMap);
            multiPut(absentMap);
        }

        return results;
    }

    @Override
    public void put(K key, V value) {
        if (null == key || null == value) {
            throw new IllegalArgumentException("null == key || null == value");
        }

        Element element = new Element(key, value);
        cache.put(element);
    }

    @Override
    public void multiPut(Map<K, V> elements) {

        if (!MapUtils.isEmpty(elements)) {
            cache.putAll(elements.entrySet()
                    .stream()
                    .map(e -> new Element(e.getKey(), e.getValue()))
                    .collect(Collectors.toList())
            );
        }
    }

    @Override
    public void evict(K key) {
        cache.remove(key);
    }

}


