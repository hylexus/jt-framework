package io.github.hylexus.jt.boot.autoconfigure.dashboard.client.impl.factory;

import io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.props.Jt808ApplicationProps;
import io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.props.JtApplicationProps;
import io.github.hylexus.jt.dashboard.client.registration.Jt808Application;
import io.github.hylexus.jt.dashboard.client.registration.JtApplication;
import io.github.hylexus.jt.dashboard.client.registration.JtApplicationFactory;

public class Jt808ApplicationFactory implements JtApplicationFactory {
    private final JtApplicationProps applicationProps;

    public Jt808ApplicationFactory(JtApplicationProps applicationProps) {
        this.applicationProps = applicationProps;
    }

    @Override
    public JtApplication createApplication() {
        final Jt808ApplicationProps jt808 = this.applicationProps.getJt808();
        return Jt808Application.builder()
                .name(jt808.getName())
                .baseUrl(jt808.getBaseUrl())
                .source("rest-api")
                .metadata(jt808.getMetadata())
                .build();
    }
}
