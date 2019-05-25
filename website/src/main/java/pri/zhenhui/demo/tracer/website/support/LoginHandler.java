package pri.zhenhui.demo.tracer.website.support;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.http.HttpServerRequest;
import io.vertx.reactivex.core.http.HttpServerResponse;
import io.vertx.reactivex.ext.auth.AuthProvider;
import io.vertx.reactivex.ext.auth.User;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.ext.web.Session;
import io.vertx.serviceproxy.ServiceException;
import org.thymeleaf.util.StringUtils;
import pri.zhenhui.demo.udms.service.exception.Errors;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class LoginHandler extends AbstractHandler {

    private static final Logger logger = LoggerFactory.getLogger(LoginHandler.class);

    private final AuthProvider authProvider;

    public static LoginHandler create(AuthProvider authProvider, AppContext appContext) {
        return new LoginHandler(authProvider, appContext);
    }

    @Override
    public void handle(RoutingContext context) {

        final HttpServerRequest request = context.request();
        final String username = request.getParam("username");
        final String password = request.getParam("password");
        if (StringUtils.isEmptyOrWhitespace(username) || StringUtils.isEmptyOrWhitespace(password)) {
            context.response()
                    .setStatusCode(400)
                    .end("Please enter username and password", "UTF-8");
            return;
        }

        if (request.method() != HttpMethod.POST) {
            context.fail(405);
        } else {
            if (!request.isExpectMultipart()) {
                throw new IllegalStateException("Form body not parsed - do you forget to include a BodyHandler?");
            }

            if (StringUtils.isEmptyOrWhitespace(username) || StringUtils.isEmptyOrWhitespace(password)) {
                context.fail(400);
            } else {
                Session session = context.session();
                JsonObject authInfo = new JsonObject().put("username", username).put("password", password);
                authProvider.authenticate(authInfo, res -> {
                    if (res.succeeded()) {
                        User user = res.result();
                        context.setUser(user);
                        if (session != null) {
                            session.regenerateId();
                            final String returnUrl = session.remove(Constants.RETURN_URL);
                            if (returnUrl != null) {
                                doRedirect(context.response(), returnUrl);
                                return;
                            }
                        }
                        doRedirect(request.response(), "/");
                    } else {
                        ServiceException exception = (ServiceException) res.cause();
                        if (exception.failureCode() == Errors.USER_NOT_FOUND.code) {
                            doRedirectLogin(context.response(), "User not found!");
                        } else if (exception.failureCode() == Errors.USER_PASSWORD_MISMATCH.code) {
                            doRedirectLogin(context.response(), "Your password mismatch!");
                        } else {
                            doRedirectLogin(context.response(), "Service Unavailable!");
                        }
                    }
                });
            }
        }
    }

    private void doRedirect(HttpServerResponse response, String redirectUrl) {
        response.setStatusCode(302)
                .putHeader("location", redirectUrl)
                .end();
    }

    private void doRedirectLogin(HttpServerResponse response, String error) {
        try {
            response.setStatusCode(302)
                    .putHeader("location", "/login?error=" + URLEncoder.encode(error, "UTF-8"))
                    .end();
        } catch (UnsupportedEncodingException e) {
            // ignore
        }
    }

    private LoginHandler(AuthProvider authProvider, AppContext appContext) {
        super(appContext);
        this.authProvider = authProvider;
    }
}

