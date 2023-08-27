package io.github.hylexus.jt.boot.autoconfigure.dashboard.server;

import io.github.hylexus.jt.core.registry.SimpleInMemoryRegistryStore;
import io.github.hylexus.jt.dashboard.server.model.values.StaticJtApplicationProps;
import io.github.hylexus.jt.dashboard.server.registry.Jt1078InstanceRegistry;
import io.github.hylexus.jt.dashboard.server.registry.Jt808InstanceRegistry;
import io.github.hylexus.jt.dashboard.server.proxy.InstanceIdGenerator;
import io.github.hylexus.jt.dashboard.server.proxy.impl.HashingInstanceUrlIdGenerator;
import io.github.hylexus.jt.dashboard.server.service.ProxyInstanceProvider;
import io.github.hylexus.jt.dashboard.server.service.StaticJtApplicationRegistrator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class JtDashboardRegistryConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public InstanceIdGenerator instanceIdGenerator() {
        return new HashingInstanceUrlIdGenerator();
    }

    @Bean
    public Jt1078InstanceRegistry jt1078InstanceRegistry(InstanceIdGenerator generator) {
        return new Jt1078InstanceRegistry(generator, new SimpleInMemoryRegistryStore<>());
    }

    @Bean
    public Jt808InstanceRegistry jt808InstanceRegistry(InstanceIdGenerator generator) {
        return new Jt808InstanceRegistry(generator, new SimpleInMemoryRegistryStore<>());
    }

    @Bean
    public ProxyInstanceProvider proxyInstanceProvider(Jt808InstanceRegistry jt808InstanceRegistry, Jt1078InstanceRegistry jt1078InstanceRegistry) {
        return new ProxyInstanceProvider(jt1078InstanceRegistry, jt808InstanceRegistry);
    }

    @Bean
    public StaticJtApplicationRegistrator staticJtApplicationRegistrator(StaticJtApplicationProps props, Jt808InstanceRegistry jt808InstanceRegistry, Jt1078InstanceRegistry jt1078InstanceRegistry) {
        return new StaticJtApplicationRegistrator(props, jt808InstanceRegistry, jt1078InstanceRegistry);
    }
}
