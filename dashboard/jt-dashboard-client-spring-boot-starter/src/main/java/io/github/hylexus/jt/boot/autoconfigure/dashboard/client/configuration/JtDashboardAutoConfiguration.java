package io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration;

import io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.jt1078.JtDashboard1078AutoConfiguration;
import io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.jt808.JtDashboard808AutoConfiguration;
import io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.props.JtApplicationProps;
import io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.reactive.JtDashboardReactiveCommonConfiguration;
import io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.servlet.JtDashboardServletCommonConfiguration;
import io.github.hylexus.jt.dashboard.client.registration.JtApplicationRegistrator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

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

    @Bean(name = "jt-dashboard-client-scheduler")
    @ConditionalOnMissingBean
    public ThreadPoolTaskScheduler registrationTaskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(1);
        taskScheduler.setRemoveOnCancelPolicy(true);
        taskScheduler.setThreadNamePrefix("jt-dashboard-client-scheduler");
        return taskScheduler;
    }

    @Bean
    public JtApplicationRegistratorListener jtApplicationRegistratorListener(
            @Qualifier("jt-dashboard-client-scheduler") ThreadPoolTaskScheduler taskScheduler,
            List<JtApplicationRegistrator> registratorList) {

        return new JtApplicationRegistratorListener(taskScheduler, registratorList);
    }

    @Slf4j
    public static class JtApplicationRegistratorListener {

        private volatile ScheduledFuture<?> scheduledTask;
        private final ThreadPoolTaskScheduler taskScheduler;

        private Duration registerPeriod = Duration.ofSeconds(10);

        private final List<JtApplicationRegistrator> registratorList;


        public JtApplicationRegistratorListener(ThreadPoolTaskScheduler taskScheduler, List<JtApplicationRegistrator> registratorList) {
            this.taskScheduler = taskScheduler;
            this.registratorList = registratorList;
        }


        @EventListener
        public void onApplicationReady(ApplicationReadyEvent event) {
            this.startRegisterTask();
        }

        @EventListener
        public void onClosedContext(ContextClosedEvent event) {
            if (event.getApplicationContext().getParent() == null
                    || "bootstrap".equals(event.getApplicationContext().getParent().getId())) {

                this.stopRegisterTask();

                this.doDeregister();
            }
        }

        public void startRegisterTask() {
            if (this.scheduledTask != null && !this.scheduledTask.isDone()) {
                return;
            }

            this.scheduledTask = taskScheduler.scheduleAtFixedRate(this::doRegister, registerPeriod);
            log.debug("Scheduled registration task for every {}ms", registerPeriod.toMillis());
        }

        public void stopRegisterTask() {
            if (scheduledTask != null && !scheduledTask.isDone()) {
                scheduledTask.cancel(true);
                log.debug("Canceled registration task");
            }
        }

        private void doRegister() {
            for (final JtApplicationRegistrator registrator : this.registratorList) {
                registrator.register();
            }
        }

        private void doDeregister() {
            for (final JtApplicationRegistrator registrator : this.registratorList) {
                try {
                    registrator.deregister();
                } catch (Throwable e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

}
