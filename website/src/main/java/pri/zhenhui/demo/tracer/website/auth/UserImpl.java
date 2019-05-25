package pri.zhenhui.demo.tracer.website.auth;

import io.reactivex.Single;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AbstractUser;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.reactivex.SingleHelper;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import pri.zhenhui.demo.udms.domain.Principal;

@SuppressWarnings("unused")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class UserImpl extends AbstractUser {

    @NonNull
    private Principal principal;

    private transient AuthProvider authProvider;

    @Override
    protected void doIsPermitted(String authority, Handler<AsyncResult<Boolean>> handler) {
        Single.<Boolean>create(emitter -> {
            try {
                emitter.onSuccess(principal.getPermissions().contains(authority));
            } catch (Throwable e) {
                emitter.onError(e);
            }
        }).subscribe(SingleHelper.toObserver(handler));
    }

    @Override
    public JsonObject principal() {
        return principal.toJson();
    }

    @Override
    public void setAuthProvider(AuthProvider authProvider) {
        this.authProvider = authProvider;
    }
}

