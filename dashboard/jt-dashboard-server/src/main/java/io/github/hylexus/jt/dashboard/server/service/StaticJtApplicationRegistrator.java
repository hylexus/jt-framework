package io.github.hylexus.jt.dashboard.server.service;

import io.github.hylexus.jt.dashboard.server.model.dto.instance.Jt1078Registration;
import io.github.hylexus.jt.dashboard.server.model.dto.instance.Jt808Registration;
import io.github.hylexus.jt.dashboard.server.model.values.StaticJt1078ApplicationProps;
import io.github.hylexus.jt.dashboard.server.model.values.StaticJt808ApplicationProps;
import io.github.hylexus.jt.dashboard.server.model.values.StaticJtApplicationProps;
import io.github.hylexus.jt.dashboard.server.registry.Jt1078InstanceRegistry;
import io.github.hylexus.jt.dashboard.server.registry.Jt808InstanceRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@Slf4j
public class StaticJtApplicationRegistrator {
    private final StaticJtApplicationProps props;
    private final Jt808InstanceRegistry jt808InstanceRegistry;
    private final Jt1078InstanceRegistry jt1078InstanceRegistry;

    public StaticJtApplicationRegistrator(StaticJtApplicationProps props, Jt808InstanceRegistry jt808InstanceRegistry, Jt1078InstanceRegistry jt1078InstanceRegistry) {
        this.props = props;
        this.jt808InstanceRegistry = jt808InstanceRegistry;
        this.jt1078InstanceRegistry = jt1078InstanceRegistry;
    }

    @EventListener
    public void onApplicationReady(ApplicationReadyEvent event) {
        if (!props.isEnabled()) {
            return;
        }
        log.info("Receive ApplicationReadyEvent: {}, register to JtDashboard", event);

        this.props.getJt808Applications().stream().map(this::convert).forEach(this.jt808InstanceRegistry::register);

        this.props.getJt1078Applications().stream().map(this::convert).forEach(this.jt1078InstanceRegistry::register);
    }

    private Jt808Registration convert(StaticJt808ApplicationProps it) {
        final Jt808Registration registration = new Jt808Registration();
        registration.setBaseUrl(it.getBaseUrl());
        registration.setName(it.getName());
        registration.setType("JT_808");
        registration.setSource("static");
        registration.setMetadata(it.getMetadata());
        return registration;
    }

    private Jt1078Registration convert(StaticJt1078ApplicationProps it) {
        final Jt1078Registration registration = new Jt1078Registration();
        registration.setName(it.getName());
        registration.setBaseUrl(it.getBaseUrl());
        registration.setType("JT_1078");
        registration.setSource("static");
        registration.setMetadata(it.getMetadata());
        registration.setTcpPort(it.getTcpPort());
        registration.setUdpPort(it.getUdpPort());
        registration.setHttpPort(it.getHttpPort());
        registration.setHost(it.getHost());
        return registration;
    }

}
