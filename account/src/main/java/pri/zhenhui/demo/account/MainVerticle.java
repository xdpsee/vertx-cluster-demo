package pri.zhenhui.demo.account;

import io.reactivex.Completable;
import io.vertx.reactivex.core.AbstractVerticle;

public class MainVerticle extends AbstractVerticle {

    @Override
    public Completable rxStart() {

        return vertx.rxDeployVerticle(new UserReadServiceVerticle())
                .ignoreElement()
                .andThen(vertx.rxDeployVerticle(new UserWriteServiceVerticle()))
                .ignoreElement()
                .andThen(vertx.rxDeployVerticle(new AuthorityReadServiceVerticle()))
                .ignoreElement()
                .andThen(vertx.rxDeployVerticle(new AuthorityWriteServiceVerticle()))
                .ignoreElement();

    }
}

