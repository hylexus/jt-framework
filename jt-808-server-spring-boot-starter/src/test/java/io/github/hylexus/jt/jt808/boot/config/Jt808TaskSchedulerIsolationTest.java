package io.github.hylexus.jt.jt808.boot.config;

import io.github.hylexus.jt.netty.JtEventExecutorGroupProvider;
import io.netty.util.concurrent.EventExecutorGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.ScheduledExecutorService;

import static io.github.hylexus.jt.jt808.JtProtocolConstant.BEAN_NAME_JT808_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * @see <a href="https://github.com/hylexus/jt-framework/issues/101">issues#101</a>
 */
@ExtendWith(OutputCaptureExtension.class)
class Jt808TaskSchedulerIsolationTest {

    @Test
    void shouldCreateApplicationTaskSchedulerWhenProviderIsPresent() {
        new ApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(TaskSchedulingAutoConfiguration.class))
                .withUserConfiguration(SchedulingConfiguration.class)
                .run(context -> {
                    assertTrue(context.containsBean("taskScheduler"));
                    assertInstanceOf(TaskScheduler.class, context.getBean("taskScheduler"));
                    assertInstanceOf(JtEventExecutorGroupProvider.class, context.getBean(BEAN_NAME_JT808_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP));
                    assertFalse(context.getBeansOfType(ScheduledExecutorService.class).containsKey(BEAN_NAME_JT808_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP));
                });
    }

    @Test
    void shouldReportLegacyExecutorBeanMigration(CapturedOutput output) {
        final SpringApplication application = new SpringApplication(LegacyExecutorConfiguration.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.setLogStartupInfo(false);
        application.setRegisterShutdownHook(false);
        application.setWebApplicationType(WebApplicationType.NONE);

        assertThrows(RuntimeException.class, application::run);
        final String lineSeparator = System.lineSeparator();
        assertTrue(output.getAll().contains(
                "Description:" + lineSeparator + lineSeparator
                        + "Bean 'jt808MsgProcessorEventExecutorGroup' uses the legacy executor contract:"
                        + lineSeparator
                        + "    Bean type: "
        ));
        assertTrue(output.getAll().contains("Legacy type: io.netty.util.concurrent.EventExecutorGroup"));
        assertTrue(output.getAll().contains("default Provider auto-configuration to back off"));
        assertTrue(output.getAll().contains(
                "Action:" + lineSeparator + lineSeparator
                        + "Keep the existing bean name:" + lineSeparator
                        + "    jt808MsgProcessorEventExecutorGroup" + lineSeparator + lineSeparator
                        + "Change the @Bean method's return type to:"
        ));
        assertTrue(output.getAll().contains("https://github.com/hylexus/jt-framework/issues/101"));
    }

    @Configuration(proxyBeanMethods = false)
    @EnableScheduling
    static class SchedulingConfiguration {

        @Bean(BEAN_NAME_JT808_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP)
        JtEventExecutorGroupProvider eventExecutorGroupProvider() {
            @SuppressWarnings("resource") final EventExecutorGroup eventExecutorGroup = mock(EventExecutorGroup.class);
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

    @Configuration(proxyBeanMethods = false)
    static class LegacyExecutorConfiguration {

        @Bean(BEAN_NAME_JT808_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP)
        EventExecutorGroup eventExecutorGroup() {
            return mock(EventExecutorGroup.class);
        }

        @Bean
        Object requiresProvider(
                @Qualifier(BEAN_NAME_JT808_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP)
                JtEventExecutorGroupProvider provider) {
            return new Object();
        }
    }

}
