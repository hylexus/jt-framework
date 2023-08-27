package io.github.hylexus.jt.boot.autoconfigure.dashboard.server;

import io.github.hylexus.jt.boot.autoconfigure.dashboard.server.reactive.JtDashboardReactiveAutoConfiguration;
import io.github.hylexus.jt.boot.autoconfigure.dashboard.server.servlet.JtDashboardServletAutoConfiguration;
import io.github.hylexus.jt.dashboard.server.model.values.StaticJtApplicationProps;
import io.github.hylexus.jt.dashboard.server.proxy.DashboardWebClient;
import io.github.hylexus.jt.dashboard.server.proxy.DashboardWebClientCustomizer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.web.reactive.function.client.WebClient;

@AutoConfiguration
@EnableConfigurationProperties({
        StaticJtApplicationProps.class,
})
@Import({
        JtDashboardRegistryConfiguration.class,
        JtDashboardControllerConfiguration.class,
        JtDashboardReactiveAutoConfiguration.class,
        JtDashboardServletAutoConfiguration.class,
        JtDashboardWebClientCustomizerConfiguration.class,
})
public class JtDashboardServerAutoConfiguration {
    private final DashboardWebClient.Builder instanceWebClientBuilder;

    public JtDashboardServerAutoConfiguration(ObjectProvider<DashboardWebClientCustomizer> customizers, WebClient.Builder webClient) {
        this.instanceWebClientBuilder = DashboardWebClient.newBuilder(webClient);
        customizers.orderedStream().forEach((customizer) -> customizer.customize(this.instanceWebClientBuilder));
    }

    @Bean
    @ConditionalOnMissingBean
    @Scope("prototype")
    public DashboardWebClient.Builder dashboardWebClientBuilder() {
        return this.instanceWebClientBuilder.clone();
    }

}
