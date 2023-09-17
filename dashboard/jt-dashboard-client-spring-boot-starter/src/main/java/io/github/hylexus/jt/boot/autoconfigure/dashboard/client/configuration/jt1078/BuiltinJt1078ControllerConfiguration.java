package io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.jt1078;

import io.github.hylexus.jt.dashboard.client.controller.jt1078.BuiltinJt1078SessionManagerController;
import io.github.hylexus.jt.jt1078.spec.Jt1078SubscriberManager;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionManager;
import org.springframework.context.annotation.Bean;

public class BuiltinJt1078ControllerConfiguration {
    @Bean
    public BuiltinJt1078SessionManagerController builtinJt1078SessionManagerController(Jt1078SubscriberManager publisherManager, Jt1078SessionManager sessionManager) {
        return new BuiltinJt1078SessionManagerController(publisherManager, sessionManager);
    }
}
