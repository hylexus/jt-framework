package io.github.hylexus.jt.jt1078.boot.configuration;

import io.github.hylexus.jt.jt1078.boot.configuration.codec.Jt1078CodecAutoConfiguration;
import io.github.hylexus.jt.jt1078.boot.configuration.handler.Jt1078HandlerAutoConfiguration;
import io.github.hylexus.jt.jt1078.boot.configuration.netty.Jt1078NettyAutoConfiguration;
import io.github.hylexus.jt.jt1078.boot.props.Jt1078ServerProps;
import io.github.hylexus.jt.jt1078.spec.Jt1078RequestLifecycleListener;
import io.github.hylexus.jt.jt1078.spec.Jt1078RequestLifecycleListenerAware;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionManager;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionManagerAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author hylexus
 */
@Configuration
@Import({
        Jt1078CommonAutoConfiguration.class,
        Jt1078CodecAutoConfiguration.class,
        Jt1078HandlerAutoConfiguration.class,
        Jt1078NettyAutoConfiguration.class,
        Jt1078AutoConfiguration.Jt1078RequestLifecycleListenerBinder.class,
        Jt1078AutoConfiguration.Jt1078SessionManagerBinder.class,
})
@EnableConfigurationProperties({
        Jt1078ServerProps.class,
})
@ConditionalOnProperty(prefix = "jt1078", name = "enabled", havingValue = "true", matchIfMissing = true)
public class Jt1078AutoConfiguration {

    @Slf4j
    static class Jt1078RequestLifecycleListenerBinder {
        public Jt1078RequestLifecycleListenerBinder(ApplicationContext applicationContext, Jt1078RequestLifecycleListener lifecycleListener) {
            this.doBind(applicationContext, lifecycleListener);
        }

        private void doBind(ApplicationContext applicationContext, Jt1078RequestLifecycleListener lifecycleListener) {
            applicationContext.getBeansOfType(Jt1078RequestLifecycleListenerAware.class).forEach((name, instance) -> {
                instance.setRequestLifecycleListener(lifecycleListener);
                log.info("--> Binding [{}] to [{}]", lifecycleListener.getClass().getName(), instance.getClass().getName());
            });
        }
    }

    @Slf4j
    static class Jt1078SessionManagerBinder {
        public Jt1078SessionManagerBinder(ApplicationContext applicationContext, Jt1078SessionManager sessionManager) {
            this.doBind(applicationContext, sessionManager);
        }

        private void doBind(ApplicationContext applicationContext, Jt1078SessionManager sessionManager) {
            applicationContext.getBeansOfType(Jt1078SessionManagerAware.class).forEach((name, instance) -> {
                instance.setJt1078SessionManager(sessionManager);
                log.info("--> Binding [{}] to [{}]", sessionManager.getClass().getName(), instance.getClass().getName());
            });
        }
    }
}
