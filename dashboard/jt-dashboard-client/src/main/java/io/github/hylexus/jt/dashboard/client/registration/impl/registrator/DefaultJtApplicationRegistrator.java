package io.github.hylexus.jt.dashboard.client.registration.impl.registrator;

import io.github.hylexus.jt.dashboard.client.registration.JtApplication;
import io.github.hylexus.jt.dashboard.client.registration.JtApplicationClient;
import io.github.hylexus.jt.dashboard.client.registration.JtApplicationFactory;
import io.github.hylexus.jt.dashboard.client.registration.JtApplicationRegistrator;

import java.util.concurrent.atomic.AtomicReference;

public class DefaultJtApplicationRegistrator implements JtApplicationRegistrator {

    private final JtApplicationFactory applicationFactory;
    private final JtApplicationClient registrationClient;
    private final AtomicReference<String> registeredId = new AtomicReference<>();
    private final String dashboardUrl;

    public DefaultJtApplicationRegistrator(JtApplicationFactory applicationFactory, JtApplicationClient registrationClient, String dashboardUrl) {
        this.applicationFactory = applicationFactory;
        this.registrationClient = registrationClient;
        this.dashboardUrl = dashboardUrl;
    }

    @Override
    public boolean register() {
        final JtApplication application = this.applicationFactory.createApplication();
        final String register = registrationClient.register(dashboardUrl, application);
        this.registeredId.set(register);
        return register != null;
    }

    @Override
    public void deregister() {
        final String id = this.registeredId.get();
        if (id == null) {
            return;
        }
        registrationClient.deregister(dashboardUrl, id);
    }
}
