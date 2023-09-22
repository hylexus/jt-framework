package io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.jt1078;

import io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.actuactor.Jt1078ActuatorConfiguration;
import io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.condition.ConditionalOnJt1078ServerPresent;
import io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.jt1078.blocking.BlockingJtDashboard1078Configuration;
import io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.jt1078.reactive.ReactiveJtDashboard1078Configuration;
import io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.props.Jt1078ApplicationProps;
import io.github.hylexus.jt.boot.autoconfigure.dashboard.client.configuration.props.JtApplicationProps;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@ConditionalOnJt1078ServerPresent
@Import({
        ReactiveJtDashboard1078Configuration.class,
        BlockingJtDashboard1078Configuration.class,
        BuiltinJt1078ControllerConfiguration.class,
        Jt1078ActuatorConfiguration.class,
})
public class JtDashboard1078AutoConfiguration {

    @Bean(name = "dashboardJt1078FlvPlayerScheduler")
    @ConditionalOnMissingBean(name = "dashboardJt1078FlvPlayerScheduler")
    public Scheduler dashboardJt1078FlvPlayerScheduler(JtApplicationProps applicationProps) {

        final Jt1078ApplicationProps.SchedulerProps schedulerProps = applicationProps.getJt1078().getBuiltinFlvPlayer().getScheduler();

        return Schedulers.newBoundedElastic(
                schedulerProps.getThreadCapacity(),
                schedulerProps.getQueuedTaskCapacity(),
                schedulerProps.getName(),
                (int) schedulerProps.getTtl().toSeconds(),
                schedulerProps.isDaemon()
        );
    }
}
