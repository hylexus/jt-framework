package io.github.hylexus.jt.dashboard.server.registry;

import io.github.hylexus.jt.core.registry.RegistryStore;
import io.github.hylexus.jt.dashboard.server.model.dto.instance.JtRegistration;
import io.github.hylexus.jt.dashboard.server.model.values.instance.Jt808Instance;
import io.github.hylexus.jt.dashboard.server.proxy.InstanceIdGenerator;
import io.github.hylexus.jt.dashboard.server.service.JtInstanceStatusConverter;

import java.time.LocalDateTime;

public class Jt808InstanceRegistry extends InstanceRegistry<Jt808Instance> {

    public Jt808InstanceRegistry(InstanceIdGenerator generator, RegistryStore<String, Jt808Instance> store, JtInstanceStatusConverter instanceStatusConverter) {
        super(generator, store, instanceStatusConverter);
    }

    @Override
    protected Jt808Instance createInstance(String id, JtRegistration jtRegistration, LocalDateTime now) {
        final Jt808Instance instance = new Jt808Instance();
        instance.setInstanceId(id);
        instance.setRegistration(jtRegistration);
        instance.setCreatedAt(now);
        return instance;
    }
}
