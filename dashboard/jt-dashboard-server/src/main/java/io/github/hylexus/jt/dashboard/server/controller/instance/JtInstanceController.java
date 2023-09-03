package io.github.hylexus.jt.dashboard.server.controller.instance;

import io.github.hylexus.jt.core.model.value.Resp;
import io.github.hylexus.jt.dashboard.server.model.values.instance.Jt1078Instance;
import io.github.hylexus.jt.dashboard.server.model.values.instance.Jt808Instance;
import io.github.hylexus.jt.dashboard.server.service.ProxyInstanceProvider;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard/instances")
public class JtInstanceController {

    private final ProxyInstanceProvider instanceProvider;

    public JtInstanceController(ProxyInstanceProvider instanceProvider) {
        this.instanceProvider = instanceProvider;
    }

    @RequestMapping("/all")
    public Resp<Object> instances() {
        final List<Jt808Instance> jt808Instances = this.instanceProvider.getJt808Instances();
        final List<Jt1078Instance> jt1078Instances = this.instanceProvider.getJt1078Instances();
        return Resp.success(Map.of(
                "jt1078ServerMetadata", jt1078Instances,
                "jt808ServerMetadata", jt808Instances
        ));
    }
}
