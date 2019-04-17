package pri.zhenhui.demo.tracer.data.verticles;

import pri.zhenhui.demo.support.AbstractMicroServiceVerticle;
import pri.zhenhui.demo.tracer.data.service.PositionReadServiceImpl;
import pri.zhenhui.demo.tracer.service.PositionReadService;

public class PositionReadServiceVerticle extends AbstractMicroServiceVerticle<PositionReadService> {

    public PositionReadServiceVerticle() {
        super(PositionReadService.SERVICE_NAME, PositionReadService.SERVICE_ADDRESS, PositionReadService.class);
    }

    @Override
    protected PositionReadService serviceImpl() {
        return new PositionReadServiceImpl(vertx.getOrCreateContext());
    }

}

