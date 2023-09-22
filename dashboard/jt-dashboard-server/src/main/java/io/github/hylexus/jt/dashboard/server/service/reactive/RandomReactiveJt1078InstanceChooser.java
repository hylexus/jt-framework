package io.github.hylexus.jt.dashboard.server.service.reactive;

import io.github.hylexus.jt.dashboard.server.model.dto.StreamAddressDto;
import io.github.hylexus.jt.dashboard.server.model.values.instance.Jt1078Instance;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomReactiveJt1078InstanceChooser implements ReactiveJt1078InstanceChooser {

    @Override
    public Mono<Jt1078Instance> chooseInstance(ServerWebExchange exchange, StreamAddressDto dto, List<Jt1078Instance> availableInstances) {
        final int size = availableInstances.size();
        if (size == 0) {
            return Mono.empty();
        }
        return Mono.just(availableInstances.get(ThreadLocalRandom.current().nextInt(size)));
    }
}
