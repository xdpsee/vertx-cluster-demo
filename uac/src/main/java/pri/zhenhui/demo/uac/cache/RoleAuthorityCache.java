package pri.zhenhui.demo.uac.cache;

import pri.zhenhui.demo.support.cache.AbstractCache;
import pri.zhenhui.demo.uac.domain.Authority;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Function;

import static java.util.stream.Collectors.toSet;

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

    public List<Authority> getsOrLoad(Set<Long> roleIds, Function<List<Long>, ArrayList<Authority>> loader) throws Exception {

        Map<Long, ArrayList<Authority>> map = multiGet(roleIds);

        final Set<Authority> authorities = new HashSet<>(map.values().stream().flatMap(Collection::stream).collect(toSet()));

        Set<Long> absentRoleIds = new HashSet<>(roleIds);
        absentRoleIds.removeAll(map.keySet());

        if (!absentRoleIds.isEmpty()) {
            authorities.addAll(loader.apply(new ArrayList<>(absentRoleIds)));
        }

        return new ArrayList<>(authorities);
    }
}
