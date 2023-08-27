package io.github.hylexus.jt.boot.autoconfigure.dashboard.client.impl.factory;

import io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.props.Jt1078ApplicationProps;
import io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.props.JtApplicationProps;
import io.github.hylexus.jt.dashboard.client.registration.Jt1078Application;
import io.github.hylexus.jt.dashboard.client.registration.JtApplication;
import io.github.hylexus.jt.dashboard.client.registration.JtApplicationFactory;

public class Jt1078ApplicationFactory implements JtApplicationFactory {
    private final JtApplicationProps applicationProps;

    public Jt1078ApplicationFactory(JtApplicationProps applicationProps) {
        this.applicationProps = applicationProps;
    }

    @Override
    public JtApplication createApplication() {
        final Jt1078ApplicationProps jt1078 = this.applicationProps.getJt1078();
        return Jt1078Application.builder()
                .name(jt1078.getName())
                .baseUrl(jt1078.getBaseUrl())
                .source("rest-api")
                .metadata(jt1078.getMetadata())
                // extra
                .host(jt1078.getHost())
                .httpPort(jt1078.getHttpPort())
                .tcpPort(jt1078.getTcpPort())
                .udpPort(jt1078.getUdpPort())
                .build();
    }
}
