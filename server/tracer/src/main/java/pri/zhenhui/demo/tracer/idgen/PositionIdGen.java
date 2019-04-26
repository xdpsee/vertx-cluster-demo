package pri.zhenhui.demo.tracer.idgen;


import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.util.Set;

public final class PositionIdGen {

    private static final String ID_POSITION = "tracer.position.sequence";

    public static Long next() {

        Set<HazelcastInstance> instances = Hazelcast.getAllHazelcastInstances();
        HazelcastInstance hazelcast = instances.stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No hazelcast instance found"));

        return hazelcast.getFlakeIdGenerator(ID_POSITION).newId();
    }

}






