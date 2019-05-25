package pri.zhenhui.demo.udms.service.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.reactivex.SingleHelper;
import io.vertx.reactivex.core.Context;
import pri.zhenhui.demo.udms.domain.Authority;
import pri.zhenhui.demo.udms.domain.Role;
import pri.zhenhui.demo.udms.domain.enums.AuthorityType;
import pri.zhenhui.demo.udms.domain.enums.RoleType;
import pri.zhenhui.demo.udms.manager.AuthorityManager;
import pri.zhenhui.demo.udms.service.AuthorityService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 * @author zhenhui
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public class AuthorityServiceImpl implements AuthorityService {

    private final Context context;

    private final AuthorityManager authorityManager = AuthorityManager.getInstance();

    public AuthorityServiceImpl(Context context) {
        this.context = context;
    }

    @Override
    public void queryRoles(Handler<AsyncResult<List<Role>>> resultHandler) {
        context.<List<Role>>executeBlocking(future -> {
            future.complete(Arrays.stream(RoleType.values()).map(Role::from).collect(toList()));
        }, resultHandler);
    }

    @Override
    public void queryAuthorities(Handler<AsyncResult<List<Authority>>> resultHandler) {
        context.<List<Authority>>executeBlocking(future -> {
            future.complete(Arrays.stream(AuthorityType.values()).map(Authority::from).collect(toList()));
        }, resultHandler);
    }

    @Override
    public void queryUserRoles(Long userId, Handler<AsyncResult<List<Role>>> resultHandler) {
        authorityManager.getUserRoles(userId)
                .subscribe(SingleHelper.toObserver(resultHandler));
    }

    @Override
    public void queryRoleAuthorities(Long roleId, Handler<AsyncResult<List<Authority>>> resultHandler) {
        authorityManager.getRoleAuthorities(roleId)
                .subscribe(SingleHelper.toObserver(resultHandler));
    }

    @Override
    public void queryUserAuthorities(Long userId, Handler<AsyncResult<List<Authority>>> resultHandler) {

        authorityManager.getUserRoles(userId)
                .map(e -> e.stream().map(Role::getId).collect(Collectors.toSet()))
                .flatMap(authorityManager::batchGetRoleAuthorities)
                .map(roleAuthorities -> {
                    Set<Authority> authorities = roleAuthorities.values().stream().flatMap(List::stream).collect(toSet());
                    return new ArrayList<>(authorities);
                }).subscribe(SingleHelper.toObserver(resultHandler));
    }

    @Override
    public void createUserRoles(Long userId, List<Role> roles, Handler<AsyncResult<Boolean>> resultHandler) {

        authorityManager.createUserRoles(userId, roles)
                .subscribe(SingleHelper.toObserver(resultHandler));

    }

    @Override
    public void deleteUserRoles(Long userId, List<Role> roles, Handler<AsyncResult<Boolean>> resultHandler) {

        authorityManager.deleteUserRoles(userId, roles)
                .subscribe(SingleHelper.toObserver(resultHandler));

    }

    @Override
    public void createRoleAuthorities(Long roleId, List<Authority> authorities, Handler<AsyncResult<Boolean>> resultHandler) {

        authorityManager.createRoleAuthorities(roleId, authorities)
                .subscribe(SingleHelper.toObserver(resultHandler));
    }

    @Override
    public void deleteRoleAuthorities(Long roleId, List<Authority> authorities, Handler<AsyncResult<Boolean>> resultHandler) {

        authorityManager.deleteRoleAuthorities(roleId, authorities)
                .subscribe(SingleHelper.toObserver(resultHandler));

    }
}
