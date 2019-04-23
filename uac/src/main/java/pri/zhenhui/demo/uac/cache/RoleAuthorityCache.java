package pri.zhenhui.demo.uac.cache;

import pri.zhenhui.demo.support.cache.AbstractCache;
import pri.zhenhui.demo.uac.domain.Authority;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class RoleAuthorityCache extends AbstractCache<Long, ArrayList<Authority>> {

    public RoleAuthorityCache() {
        super(CacheName.ROLE_AUTHORITY_CACHE);
    }

    public List<Authority> getOrLoad(Long roleId, Callable<ArrayList<Authority>> loader) throws Exception {

        ArrayList<Authority> authorities = get(roleId);
        if (authorities != null) {
            return authorities;
        }

        authorities = loader.call();
        if (authorities != null) {
            put(roleId, authorities);
        }

        return authorities;
    }
}
