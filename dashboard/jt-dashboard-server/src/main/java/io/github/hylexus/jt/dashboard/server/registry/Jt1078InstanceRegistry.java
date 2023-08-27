package io.github.hylexus.jt.dashboard.server.registry;

import io.github.hylexus.jt.core.registry.RegistryStore;
import io.github.hylexus.jt.dashboard.server.model.dto.instance.JtRegistration;
import io.github.hylexus.jt.dashboard.server.model.values.instance.Jt1078Instance;
import io.github.hylexus.jt.dashboard.server.proxy.InstanceIdGenerator;

public class Jt1078InstanceRegistry extends InstanceRegistry<Jt1078Instance> {

    public Jt1078InstanceRegistry(InstanceIdGenerator generator, RegistryStore<String, Jt1078Instance> store) {
        super(generator, store);
    }

    @Override
    protected Jt1078Instance createInstance(String id, JtRegistration jtRegistration) {
        final Jt1078Instance instance = new Jt1078Instance();
        instance.setInstanceId(id);
        instance.setRegistration(jtRegistration);
        return instance;
    }
}
