package io.github.hylexus.jt.dashboard.server.service.reactive;

import io.github.hylexus.jt.dashboard.server.model.dto.StreamAddressDto;
import io.github.hylexus.jt.dashboard.server.model.values.instance.Jt1078Instance;
import io.github.hylexus.jt.dashboard.server.service.Jt1078InstanceChooser;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ReactiveJt1078InstanceChooser extends Jt1078InstanceChooser {
    Mono<Jt1078Instance> chooseInstance(ServerWebExchange exchange, StreamAddressDto dto, List<Jt1078Instance> availableInstances);
}
