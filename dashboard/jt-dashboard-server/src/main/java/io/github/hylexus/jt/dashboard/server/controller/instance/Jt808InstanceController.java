package io.github.hylexus.jt.dashboard.server.controller.instance;

import io.github.hylexus.jt.dashboard.server.model.dto.instance.Jt808Registration;
import io.github.hylexus.jt.dashboard.server.model.values.instance.Jt808Instance;
import io.github.hylexus.jt.dashboard.server.registry.Jt808InstanceRegistry;
import io.github.hylexus.jt.model.value.Resp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/dashboard/instances")
public class Jt808InstanceController {

    private final Jt808InstanceRegistry instanceRegistry;

    public Jt808InstanceController(Jt808InstanceRegistry instanceRegistry) {
        this.instanceRegistry = instanceRegistry;
    }

    @PostMapping("/808")
    public Map<String, String> register(@RequestBody Jt808Registration registration) {

        registration.setSource("http-api");

        final String id = instanceRegistry.register(registration);
        return Map.of("id", id);
    }

    @GetMapping(path = "/808")
    public Resp<Object> instances() {
        return Resp.success(instanceRegistry.getInstances());
    }

    @GetMapping(path = "/808/{id}")
    public Resp<Jt808Instance> instance(@PathVariable String id) {
        return instanceRegistry.getInstance(id)
                .map(Resp::success)
                .orElseGet(Resp::notFound);
    }

    @DeleteMapping(path = "/808/{id}")
    public Resp<Map<String, String>> deregister(@PathVariable String id) {
        instanceRegistry.deregister(id);
        return Resp.success(Map.of("id", id));
    }
}
