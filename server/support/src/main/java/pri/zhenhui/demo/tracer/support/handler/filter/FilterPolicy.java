package pri.zhenhui.demo.tracer.support.handler.filter;


import pri.zhenhui.demo.tracer.domain.Position;

public interface FilterPolicy {

    boolean accept(Position currPos, Position lastPos);

}
