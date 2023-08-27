package io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration;

import io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.jt1078.JtDashboard1078AutoConfiguration;
import io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.jt808.JtDashboard808AutoConfiguration;
import io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.props.JtApplicationProps;
import io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.reactive.JtDashboardReactiveCommonConfiguration;
import io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.servlet.JtDashboardServletCommonConfiguration;
import io.github.hylexus.jt.dashboard.client.registration.JtApplicationRegistrator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;

import java.util.List;

@AutoConfiguration
@EnableConfigurationProperties({
        JtApplicationProps.class,
})
@Import({
        JtDashboard808AutoConfiguration.class,
        JtDashboard1078AutoConfiguration.class,
        JtDashboardReactiveCommonConfiguration.class,
        JtDashboardServletCommonConfiguration.class,
})
public class JtDashboardAutoConfiguration {

    @Bean
    public JtApplicationRegistratorListener jtApplicationRegistratorListener(List<JtApplicationRegistrator> registratorList) {
        return new JtApplicationRegistratorListener(registratorList);
    }

    @Slf4j
    public static class JtApplicationRegistratorListener {
        private final List<JtApplicationRegistrator> registratorList;


        public JtApplicationRegistratorListener(List<JtApplicationRegistrator> registratorList) {
            this.registratorList = registratorList;
        }

        @EventListener
        public void onApplicationReady(ApplicationReadyEvent event) {
            log.info("Receive ApplicationReadyEvent: {}, register to JtDashboard", event);
            for (final JtApplicationRegistrator registrator : this.registratorList) {
                registrator.register();
            }
        }

        @EventListener
        public void onClosedContext(ContextClosedEvent event) {
            log.info("Receive ContextClosedEvent: {}, deregister from JtDashboard", event);
            for (final JtApplicationRegistrator registrator : this.registratorList) {
                registrator.deregister();
            }
        }
    }

}
