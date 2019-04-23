package pri.zhenhui.demo.uac.cache;

import pri.zhenhui.demo.support.cache.AbstractCache;
import pri.zhenhui.demo.support.cache.Cache;
import pri.zhenhui.demo.uac.domain.User;

import java.io.Serializable;
import java.util.concurrent.Callable;

public class UserCache {

    private final Cache<Long, User> cacheById;

    private final Cache<String, User> cacheByName;

    private final Cache<String, User> cacheByPhone;

    public UserCache() {
        cacheById = new AbstractCache<Long, User>(CacheName.USER_CACHE_BY_ID){};

        cacheByName = new AbstractCache<String, User>(CacheName.USER_CACHE_BY_NAME){};

        cacheByPhone = new AbstractCache<String, User>(CacheName.USER_CACHE_BY_PHONE){};
    }

    public User getUserById(Long userId, Callable<User> loader) throws Exception {

        return getUser(cacheById, userId, loader);

    }

    public User getUserByName(String username, Callable<User> loader) throws Exception {

        return getUser(cacheByName, username, loader);

    }

    public User getUserByPhone(String phone, Callable<User> loader) throws Exception {

        return getUser(cacheByPhone, phone, loader);

    }

    private static  <K extends Serializable, V extends Serializable> V getUser(Cache<K, V> cache, K key, Callable<V> loader) throws Exception {

        V user = cache.get(key);
        if (user != null) {
            return user;
        }

        user = loader.call();
        if (user != null) {
            cache.put(key, user);
        }

        return user;

    }

    public void evict(Long userId, String username, String phone) {

        if (userId != null) {
            cacheById.evict(userId);
        }

        if (username != null) {
            cacheByName.evict(username);
        }

        if (phone != null) {
            cacheByPhone.evict(phone);
        }
    }


}


