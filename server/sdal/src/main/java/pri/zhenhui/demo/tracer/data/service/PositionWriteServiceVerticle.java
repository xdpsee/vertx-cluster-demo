package pri.zhenhui.demo.tracer.data.service;

import pri.zhenhui.demo.support.AbstractMicroServiceVerticle;
import pri.zhenhui.demo.tracer.service.PositionWriteService;

public class PositionWriteServiceVerticle extends AbstractMicroServiceVerticle<PositionWriteService> {

    public PositionWriteServiceVerticle() {
        super(PositionWriteService.SERVICE_NAME, PositionWriteService.SERVICE_ADDRESS, PositionWriteService.class);
    }

    @Override
    protected PositionWriteService serviceImpl() {
        return new PositionWriteServiceImpl(vertx.getOrCreateContext());
    }

}
