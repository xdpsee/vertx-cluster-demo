package pri.zhenhui.demo.tracer.data.verticles;

import pri.zhenhui.demo.support.AbstractMicroServiceVerticle;
import pri.zhenhui.demo.tracer.data.service.EventWriteServiceImpl;
import pri.zhenhui.demo.tracer.service.EventWriteService;

public class EventWriteServiceVerticle extends AbstractMicroServiceVerticle<EventWriteService> {

    public EventWriteServiceVerticle() {
        super(EventWriteService.SERVICE_NAME, EventWriteService.SERVICE_ADDRESS, EventWriteService.class);
    }

    @Override
    protected EventWriteService serviceImpl() {
        return new EventWriteServiceImpl(vertx.getOrCreateContext());
    }
}
