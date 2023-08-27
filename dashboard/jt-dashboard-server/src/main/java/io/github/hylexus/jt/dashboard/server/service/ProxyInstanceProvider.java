package io.github.hylexus.jt.dashboard.server.service;

import io.github.hylexus.jt.dashboard.server.model.values.instance.Jt1078Instance;
import io.github.hylexus.jt.dashboard.server.model.values.instance.Jt808Instance;
import io.github.hylexus.jt.dashboard.server.model.values.instance.JtInstance;
import io.github.hylexus.jt.dashboard.server.registry.Jt1078InstanceRegistry;
import io.github.hylexus.jt.dashboard.server.registry.Jt808InstanceRegistry;

import java.util.List;
import java.util.Optional;

public class ProxyInstanceProvider {
    private final Jt1078InstanceRegistry jt1078InstanceRegistry;
    private final Jt808InstanceRegistry jt808InstanceRegistry;

    public ProxyInstanceProvider(Jt1078InstanceRegistry jt1078InstanceRegistry, Jt808InstanceRegistry jt808InstanceRegistry) {
        this.jt1078InstanceRegistry = jt1078InstanceRegistry;
        this.jt808InstanceRegistry = jt808InstanceRegistry;
    }

    public List<Jt808Instance> getJt808Instances() {
        return this.jt808InstanceRegistry.getInstances();
    }

    public List<Jt1078Instance> getJt1078Instances() {
        return this.jt1078InstanceRegistry.getInstances();
    }

    public Optional<JtInstance> getInstance(String id) {
        final Optional<JtInstance> instance = this.jt1078InstanceRegistry.getInstance(id).map(JtInstance.class::cast);
        if (instance.isPresent()) {
            return instance;
        }

        return this.jt808InstanceRegistry.getInstance(id).map(JtInstance.class::cast);
    }
}
