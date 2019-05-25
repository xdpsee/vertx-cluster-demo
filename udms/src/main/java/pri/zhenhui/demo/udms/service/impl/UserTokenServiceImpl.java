package pri.zhenhui.demo.udms.service.impl;

import io.reactivex.Single;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.jwt.JWTOptions;
import io.vertx.reactivex.SingleHelper;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.auth.jwt.JWTAuth;
import org.mindrot.jbcrypt.BCrypt;
import pri.zhenhui.demo.udms.domain.*;
import pri.zhenhui.demo.udms.manager.AccountManager;
import pri.zhenhui.demo.udms.manager.AuthorityManager;
import pri.zhenhui.demo.udms.service.UserTokenService;
import pri.zhenhui.demo.udms.service.exception.Errors;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhenhui
 */
public class UserTokenServiceImpl implements UserTokenService {

    private final AccountManager accountManager;

    private final AuthorityManager authorityManager;

    private final JWTAuth jwtAuth;

    public UserTokenServiceImpl(Vertx vertx) {
        this.accountManager = AccountManager.getInstance();
        this.authorityManager = AuthorityManager.getInstance();
        this.jwtAuth = JWTAuth.create(vertx, new JWTAuthOptions()
                .addPubSecKey(new PubSecKeyOptions()
                        .setAlgorithm("HS256")
                        .setPublicKey("XmaDFytPLM4C@6MJ")
                        .setSymmetric(true)));
    }

    @Override
    public void generateToken(String username, String password, Handler<AsyncResult<UserToken>> resultHandler) {

        accountManager.getUserByName(username)
                .flatMap(opt -> {
                    if (!opt.isPresent()) {
                        return Single.error(Errors.USER_NOT_FOUND.exception());
                    }
                    User user = opt.get();
                    if (!BCrypt.checkpw(password, user.getPassword())) {
                        return Single.error(Errors.USER_PASSWORD_MISMATCH.exception());
                    }
                    return Single.just(user);
                })
                .flatMap(user -> createToken(user.getId(), username))
                .subscribe(SingleHelper.toObserver(resultHandler));

    }

    @Override
    public void validateToken(String token, Handler<AsyncResult<JsonObject>> resultHandler) {
        jwtAuth.authenticate(new JsonObject().put("jwt", token), authenticate -> {
            if (authenticate.succeeded()) {
                resultHandler.handle(Future.succeededFuture(authenticate.result().principal()));
            } else {
                resultHandler.handle(Future.failedFuture(authenticate.cause()));
            }
        });
    }

    @Override
    public void refreshToken(String token, Handler<AsyncResult<String>> resultHandler) {
        jwtAuth.rxAuthenticate(new JsonObject().put("jwt", token))
                .flatMap(user -> {
                    JsonObject principal = user.principal();
                    return createToken(principal.getLong("uid"), principal.getString("sub"));
                })
                .map(UserToken::getToken)
                .subscribe(SingleHelper.toObserver(resultHandler));
    }

    @Override
    public void blockToken(String token, Handler<AsyncResult<Boolean>> resultHandler) {

    }

    private Single<UserToken> createToken(long userId, String username) {

        return authorityManager.getUserRoles(userId)
                .map(e -> e.stream().map(Role::getId).collect(Collectors.toSet()))
                .flatMap(authorityManager::batchGetRoleAuthorities)
                .flatMap(roleAuthorities -> {
                    try {
                        List<String> permissions = roleAuthorities.values().stream()
                                .flatMap(List::stream)
                                .distinct()
                                .map(Authority::getTitle)
                                .collect(Collectors.toList());

                        JWTOptions options = new JWTOptions().setPermissions(permissions);
                        Principal principal = new Principal(userId, username, permissions);
                        UserToken token = new UserToken();

                        JsonObject claims = new JsonObject()
                                .put("sub", username)
                                .put("uid", userId);
                        token.setToken(jwtAuth.generateToken(claims, options));
                        token.setPrincipal(principal);
                        return Single.just(token);
                    } catch (Throwable e) {
                        return Single.error(e);
                    }
                });
    }
}
