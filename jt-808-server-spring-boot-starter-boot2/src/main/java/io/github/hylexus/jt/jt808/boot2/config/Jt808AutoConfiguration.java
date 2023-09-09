package io.github.hylexus.jt.jt808.boot2.config;

import io.github.hylexus.jt.core.OrderedComponent;
import io.github.hylexus.jt.jt808.boot.config.condition.BuiltinComponentType;
import io.github.hylexus.jt.jt808.boot.config.condition.ConditionalOnJt808BuiltinComponentsEnabled;
import io.github.hylexus.jt.jt808.boot.config.configuration.Jt808DispatcherHandlerAutoConfiguration;
import io.github.hylexus.jt.jt808.boot.config.configuration.Jt808NettyServerAutoConfiguration;
import io.github.hylexus.jt.jt808.boot.config.configuration.Jt808ServerComponentStatistics;
import io.github.hylexus.jt.jt808.boot.config.configuration.Jt808SubPackageAutoConfiguration;
import io.github.hylexus.jt.jt808.boot.config.configuration.codec.Jt808CodecAutoConfiguration;
import io.github.hylexus.jt.jt808.boot.props.Jt808ServerProps;
import io.github.hylexus.jt.jt808.spec.Jt808CommandSender;
import io.github.hylexus.jt.jt808.spec.Jt808MsgTypeParser;
import io.github.hylexus.jt.jt808.spec.Jt808RequestLifecycleListener;
import io.github.hylexus.jt.jt808.spec.Jt808RequestLifecycleListenerAware;
import io.github.hylexus.jt.jt808.spec.impl.BuiltinJt808MsgTypeParser;
import io.github.hylexus.jt.jt808.spec.impl.DefaultJt808CommandSender;
import io.github.hylexus.jt.jt808.spec.impl.Jt808RequestLifecycleListeners;
import io.github.hylexus.jt.jt808.spec.session.Jt808SessionManager;
import io.github.hylexus.jt.jt808.spec.session.Jt808SessionManagerAware;
import io.github.hylexus.jt.jt808.support.annotation.codec.Jt808AnnotationBasedEncoder;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgEncoder;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.builtin.BuiltinCommonHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.builtin.BuiltinTerminalAuthenticationMsgHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.builtin.BuiltinTerminalRegisterMsgHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hylexus
 */
@Import({
        Jt808SubPackageAutoConfiguration.class,
        Jt808CodecAutoConfiguration.class,
        Jt808DispatcherHandlerAutoConfiguration.class,
        Jt808NettyServerAutoConfiguration.class,
        Jt808AutoConfiguration.Jt808RequestLifecycleListenerBinder.class,
        Jt808AutoConfiguration.Jt808SessionManagerBinder.class,
})
@EnableConfigurationProperties({Jt808ServerProps.class})
public class Jt808AutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public Jt808MsgTypeParser msgTypeParser() {
        return new BuiltinJt808MsgTypeParser();
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt808CommandSender jt808CommandSender(
            Jt808SessionManager sessionManager, Jt808MsgEncoder encoder,
            Jt808AnnotationBasedEncoder annotationBasedEncoder) {
        return new DefaultJt808CommandSender(sessionManager, encoder, annotationBasedEncoder);
    }

    @Bean
    @ConditionalOnJt808BuiltinComponentsEnabled(BuiltinComponentType.REQUEST_HANDLER)
    public BuiltinTerminalRegisterMsgHandler builtinJt808RequestHandlerForDebugging() {
        return new BuiltinTerminalRegisterMsgHandler();
    }

    @Bean
    @ConditionalOnJt808BuiltinComponentsEnabled(BuiltinComponentType.REQUEST_HANDLER)
    public BuiltinTerminalAuthenticationMsgHandler builtinTerminalAuthenticationMsgHandlerForDebugging() {
        return new BuiltinTerminalAuthenticationMsgHandler();
    }

    @Bean
    @ConditionalOnJt808BuiltinComponentsEnabled(BuiltinComponentType.REQUEST_HANDLER)
    public BuiltinCommonHandler builtinCommonHandler() {
        return new BuiltinCommonHandler();
    }

    @Bean
    @ConditionalOnJt808BuiltinComponentsEnabled(BuiltinComponentType.COMPONENT_STATISTICS)
    public Jt808ServerComponentStatistics jt808ServerComponentStatistics() {
        return new Jt808ServerComponentStatistics();
    }

    @Bean
    @Primary
    public Jt808RequestLifecycleListener jt808RequestLifecycleListener(List<Jt808RequestLifecycleListener> listeners) {
        final List<Jt808RequestLifecycleListener> sortedListeners = listeners.stream()
                .sorted(Comparator.comparing(OrderedComponent::getOrder))
                .collect(Collectors.toList());

        return new Jt808RequestLifecycleListeners(sortedListeners);
    }

    @Slf4j
    static class Jt808RequestLifecycleListenerBinder {
        public Jt808RequestLifecycleListenerBinder(ApplicationContext applicationContext, Jt808RequestLifecycleListener lifecycleListener) {
            this.doBind(applicationContext, lifecycleListener);
        }

        private void doBind(ApplicationContext applicationContext, Jt808RequestLifecycleListener lifecycleListener) {
            applicationContext.getBeansOfType(Jt808RequestLifecycleListenerAware.class).forEach((name, instance) -> {
                instance.setRequestLifecycleListener(lifecycleListener);
                log.info("--> Binding [{}] to [{}]", lifecycleListener.getClass().getName(), instance.getClass().getName());
            });
        }
    }

    @Slf4j
    static class Jt808SessionManagerBinder {
        public Jt808SessionManagerBinder(ApplicationContext applicationContext, Jt808SessionManager sessionManager) {
            this.doBind(applicationContext, sessionManager);
        }

        private void doBind(ApplicationContext applicationContext, Jt808SessionManager sessionManager) {
            applicationContext.getBeansOfType(Jt808SessionManagerAware.class).forEach((name, instance) -> {
                instance.setJt808SessionManager(sessionManager);
                log.info("--> Binding [{}] to [{}]", sessionManager.getClass().getName(), instance.getClass().getName());
            });
        }
    }
}
