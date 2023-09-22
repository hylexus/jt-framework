package io.github.hylexus.jt.boot.autoconfigure.dashboard.server;

import io.github.hylexus.jt.core.registry.SimpleInMemoryRegistryStore;
import io.github.hylexus.jt.dashboard.server.model.values.StaticJtApplicationProps;
import io.github.hylexus.jt.dashboard.server.proxy.DashboardWebClient;
import io.github.hylexus.jt.dashboard.server.proxy.InstanceIdGenerator;
import io.github.hylexus.jt.dashboard.server.proxy.impl.HashingInstanceUrlIdGenerator;
import io.github.hylexus.jt.dashboard.server.registry.Jt1078InstanceRegistry;
import io.github.hylexus.jt.dashboard.server.registry.Jt808InstanceRegistry;
import io.github.hylexus.jt.dashboard.server.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.time.Duration;

@Slf4j
public class JtDashboardRegistryConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public InstanceIdGenerator instanceIdGenerator() {
        return new HashingInstanceUrlIdGenerator();
    }

    @Bean
    @ConditionalOnMissingBean
    public JtInstanceStatusConverter jtInstanceStatusConverter() {
        return JtInstanceStatusConverter.DEFAULT;
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt1078InstanceRegistry jt1078InstanceRegistry(InstanceIdGenerator generator, JtInstanceStatusConverter instanceStatusConverter) {
        return new Jt1078InstanceRegistry(generator, new SimpleInMemoryRegistryStore<>(), instanceStatusConverter);
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt808InstanceRegistry jt808InstanceRegistry(InstanceIdGenerator generator, JtInstanceStatusConverter instanceStatusConverter) {
        return new Jt808InstanceRegistry(generator, new SimpleInMemoryRegistryStore<>(), instanceStatusConverter);
    }

    @Bean
    @ConditionalOnMissingBean
    public ProxyInstanceProvider proxyInstanceProvider(Jt808InstanceRegistry jt808InstanceRegistry, Jt1078InstanceRegistry jt1078InstanceRegistry) {
        return new ProxyInstanceProvider(jt1078InstanceRegistry, jt808InstanceRegistry);
    }

    @Bean
    @ConditionalOnMissingBean
    public StaticJtApplicationRegistrator staticJtApplicationRegistrator(StaticJtApplicationProps props, Jt808InstanceRegistry jt808InstanceRegistry, Jt1078InstanceRegistry jt1078InstanceRegistry) {
        return new StaticJtApplicationRegistrator(props, jt808InstanceRegistry, jt1078InstanceRegistry);
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt808ServerSimpleMetricsUpdater jt808ServerSimpleMetricsUpdater(Jt808InstanceRegistry registry, DashboardWebClient.Builder builder) {
        return new Jt808ServerSimpleMetricsUpdater(registry, builder);
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt1078ServerSimpleMetricsUpdater jt1078ServerSimpleMetricsUpdater(Jt1078InstanceRegistry registry, DashboardWebClient.Builder builder) {
        return new Jt1078ServerSimpleMetricsUpdater(registry, builder);
    }

    @Bean(initMethod = "start", destroyMethod = "stop", name = "jt808ServerSimpleMetricsIntervalCheck")
    @ConditionalOnBean(Jt808ServerSimpleMetricsUpdater.class)
    public IntervalCheck intervalCheck(Jt808ServerSimpleMetricsUpdater updater) {
        return new IntervalCheck("simple-metrics-updater-808", instanceId -> {
            log.debug("simple-metrics-updater ::::::: instanceId={}", instanceId);
            return updater.updateStatus(instanceId);
        }, Duration.ofSeconds(10));
    }

    @ConditionalOnBean(Jt1078ServerSimpleMetricsUpdater.class)
    @Bean(initMethod = "start", destroyMethod = "stop", name = "jt1078ServerSimpleMetricsIntervalCheck")
    public IntervalCheck intervalCheck(Jt1078ServerSimpleMetricsUpdater updater) {
        return new IntervalCheck("simple-metrics-updater-1078", instanceId -> {
            log.debug("simple-metrics-updater ::::::: instanceId={}", instanceId);
            return updater.updateMetrics(instanceId);
        }, Duration.ofSeconds(10));
    }

}
