package io.github.hylexus.jt.jt1078.boot.configuration;

import io.github.hylexus.jt.netty.JtEventExecutorGroupProvider;
import io.netty.util.concurrent.EventExecutorGroup;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.ScheduledExecutorService;

import static io.github.hylexus.jt.jt1078.Jt1078ProtocolConstant.BEAN_NAME_1078_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * @see <a href="https://github.com/hylexus/jt-framework/issues/101">issues#101</a>
 */
class Jt1078TaskSchedulerIsolationTest {

    @Test
    void shouldCreateApplicationTaskSchedulerWhenProviderIsPresent() {
        new ApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(TaskSchedulingAutoConfiguration.class))
                .withUserConfiguration(SchedulingConfiguration.class)
                .run(context -> {
                    assertTrue(context.containsBean("taskScheduler"));
                    assertInstanceOf(TaskScheduler.class, context.getBean("taskScheduler"));
                    assertInstanceOf(JtEventExecutorGroupProvider.class, context.getBean(BEAN_NAME_1078_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP));
                    assertFalse(context.getBeansOfType(ScheduledExecutorService.class).containsKey(BEAN_NAME_1078_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP));
                });
    }

    @Configuration(proxyBeanMethods = false)
    @EnableScheduling
    static class SchedulingConfiguration {

        @Bean(BEAN_NAME_1078_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP)
        JtEventExecutorGroupProvider eventExecutorGroupProvider() {
            final EventExecutorGroup eventExecutorGroup = mock(EventExecutorGroup.class);
            return () -> eventExecutorGroup;
        }

        @Bean
        ScheduledComponent scheduledComponent() {
            return new ScheduledComponent();
        }

    }

    static class ScheduledComponent {

        @Scheduled(fixedDelay = 60_000)
        void scheduledTask() {
        }

    }

}
