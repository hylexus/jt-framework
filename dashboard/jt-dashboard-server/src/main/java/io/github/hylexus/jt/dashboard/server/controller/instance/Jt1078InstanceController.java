package io.github.hylexus.jt.dashboard.server.controller.instance;

import io.github.hylexus.jt.dashboard.server.model.dto.instance.Jt1078Registration;
import io.github.hylexus.jt.dashboard.server.model.values.instance.Jt1078Instance;
import io.github.hylexus.jt.dashboard.server.registry.Jt1078InstanceRegistry;
import io.github.hylexus.jt.core.model.value.Resp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/dashboard/instances")
public class Jt1078InstanceController {

    private final Jt1078InstanceRegistry instanceRegistry;

    public Jt1078InstanceController(Jt1078InstanceRegistry instanceRegistry) {
        this.instanceRegistry = instanceRegistry;
    }

    @PostMapping("/1078")
    public Resp<Object> register(@RequestBody Jt1078Registration registration) {

        registration.setSource("http-api");

        final String id = instanceRegistry.register(registration);
        return Resp.success(Map.of("id", id));
    }

    @GetMapping(path = "/1078")
    public Resp<Object> instances(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.ACCEPTED);
        return Resp.success(instanceRegistry.getInstances());
    }

    @GetMapping(path = "/1078/{id}")
    public Resp<Jt1078Instance> instance(@PathVariable("id") String id) {
        return instanceRegistry.getInstance(id)
                .map(Resp::success)
                .orElseGet(Resp::notFound);
    }

    @DeleteMapping(path = "/1078/{id}")
    public Resp<Object> deregister(@PathVariable("id") String id) {
        instanceRegistry.deregister(id);
        return Resp.success(Map.of("id", id));
    }
}
