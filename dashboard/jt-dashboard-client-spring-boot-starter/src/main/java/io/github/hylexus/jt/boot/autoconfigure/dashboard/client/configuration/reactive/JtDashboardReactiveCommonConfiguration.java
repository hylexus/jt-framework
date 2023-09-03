package io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.reactive;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;

@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class JtDashboardReactiveCommonConfiguration {

}
