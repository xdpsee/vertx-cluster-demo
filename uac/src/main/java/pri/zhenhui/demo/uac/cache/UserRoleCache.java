package pri.zhenhui.demo.uac.cache;

import pri.zhenhui.demo.support.cache.AbstractCache;
import pri.zhenhui.demo.uac.domain.Role;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class UserRoleCache extends AbstractCache<Long, ArrayList<Role>> {

    public UserRoleCache() {
        super(CacheName.USER_ROLE_CACHE);
    }

    public ArrayList<Role> getOrLoad(Long userId, Callable<ArrayList<Role>> loader) throws Exception {

        ArrayList<Role> roles = get(userId);
        if (roles != null) {
            return roles;
        }

        roles = loader.call();
        if (roles != null) {
            put(userId, roles);
        }

        return roles;
    }

}


