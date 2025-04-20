package io.github.hylexus.jt.jt808.boot.config.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.hylexus.jt.core.OrderedComponent;
import io.github.hylexus.jt.jt808.boot.config.condition.ConditionalOnJt808Server;
import io.github.hylexus.jt.jt808.boot.config.configuration.codec.Jt808CodecAutoConfiguration;
import io.github.hylexus.jt.jt808.boot.config.configuration.dispatcher.Jt808DispatcherHandlerAutoConfiguration;
import io.github.hylexus.jt.jt808.boot.props.Jt808ServerProps;
import io.github.hylexus.jt.jt808.spec.*;
import io.github.hylexus.jt.jt808.spec.impl.BuiltinJt808MsgTypeParser;
import io.github.hylexus.jt.jt808.spec.impl.Jt808RequestLifecycleListeners;
import io.github.hylexus.jt.jt808.spec.impl.request.queue.DefaultJt808RequestMsgQueueListener;
import io.github.hylexus.jt.jt808.spec.impl.request.queue.FilteringJt808RequestMsgQueueListener;
import io.github.hylexus.jt.jt808.spec.session.DefaultJt808SessionManager;
import io.github.hylexus.jt.jt808.spec.session.Jt808FlowIdGeneratorFactory;
import io.github.hylexus.jt.jt808.spec.session.Jt808SessionEventListener;
import io.github.hylexus.jt.jt808.spec.session.Jt808SessionManager;
import io.github.hylexus.jt.jt808.support.codec.Jt808RequestRouteExceptionHandler;
import io.github.hylexus.jt.jt808.support.codec.Jt808RequestSubPackageEventListener;
import io.github.hylexus.jt.jt808.support.codec.Jt808RequestSubPackageStorage;
import io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808RequestRouteExceptionHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808DispatcherHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808RequestMsgDispatcher;
import io.github.hylexus.jt.jt808.support.dispatcher.impl.DefaultJt808RequestMsgDispatcher;
import io.github.hylexus.jt.netty.JtServerNettyConfigure;
import io.github.hylexus.jt.netty.SpringEnvironmentConfigurationProvider;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ConditionalOnJt808Server(type = ConditionalOnJt808Server.Type.ANY)
@Import({
        Jt808SubPackageAutoConfiguration.class,
        Jt808CodecAutoConfiguration.class,
        Jt808DispatcherHandlerAutoConfiguration.class,
        Jt808ServerCommonAutoConfiguration.Jt808RequestLifecycleListenerBinder.class,
        Jt808ServerCommonAutoConfiguration.Jt808ServerParamPrinterConfig.class
})
public class Jt808ServerCommonAutoConfiguration {
    private static final Logger log = LoggerFactory.getLogger(Jt808ServerCommonAutoConfiguration.class);
    private final Jt808ServerProps serverProps;

    public Jt808ServerCommonAutoConfiguration(Jt808ServerProps serverProps) {
        this.serverProps = serverProps;
    }

    @Bean
    @ConditionalOnMissingBean
    JtServerNettyConfigure.ConfigurationProvider configurationProvider(Environment environment) {
        return new SpringEnvironmentConfigurationProvider(environment);
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt808MsgTypeParser msgTypeParser() {
        return new BuiltinJt808MsgTypeParser();
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt808FlowIdGeneratorFactory jt808FlowIdGeneratorFactory() {
        return new Jt808FlowIdGeneratorFactory.DefaultJt808FlowIdGeneratorFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt808SessionManager jt808SessionManager(ObjectProvider<Jt808SessionEventListener> listeners, Jt808FlowIdGeneratorFactory factory) {
        final Jt808SessionManager manager = DefaultJt808SessionManager.getInstance(factory);
        listeners.stream().sorted(Comparator.comparing(OrderedComponent::getOrder)).forEach(manager::addListener);
        return manager;
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt808RequestRouteExceptionHandler jt808RequestRouteExceptionHandler() {
        return new DefaultJt808RequestRouteExceptionHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt808RequestMsgQueueListener msgQueueListener(
            @Autowired(required = false) Jt808ServerSchedulerFactory jt808ServerSchedulerFactory,
            Jt808DispatcherHandler dispatcherHandler,
            // Jt808SessionManager sessionManager,
            Jt808RequestSubPackageStorage subPackageStorage,
            Jt808RequestSubPackageEventListener requestSubPackageEventListener,
            ObjectProvider<Jt808RequestFilter> filters) {

        final Jt808RequestMsgQueueListener delegate;
        if (this.serverProps.getFeatures().getRequestFilter().isEnabled()) {
            final List<Jt808RequestFilter> allFilters = filters.orderedStream().collect(Collectors.toList());
            delegate = new FilteringJt808RequestMsgQueueListener(dispatcherHandler, subPackageStorage, requestSubPackageEventListener, allFilters);
        } else {
            delegate = new DefaultJt808RequestMsgQueueListener(dispatcherHandler, subPackageStorage, requestSubPackageEventListener);
        }

        if (jt808ServerSchedulerFactory == null) {
            return delegate;
        }

        return new Jt808RequestMsgQueueListenerAsyncWrapper(delegate, jt808ServerSchedulerFactory.getMsgHandlerExecutor());
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt808RequestMsgDispatcher requestMsgDispatcher(Jt808RequestMsgQueueListener listener) {
        return new DefaultJt808RequestMsgDispatcher(listener);
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

    @ConditionalOnProperty(prefix = "jt808.features.program-param-printer", name = "enabled", havingValue = "true")
    // @ConditionalOnClass({ObjectMapper.class, JavaTimeModule.class})
    static class Jt808ServerParamPrinterConfig {
        @Bean
        public CommandLineRunner jt808ServerParamPrinter(Jt808ServerProps props) {
            return args -> {
                final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
                if (props.getFeatures().getProgramParamPrinter().isPretty()) {
                    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
                }
                log.info("Jt808 server config ::: {}", objectMapper.writeValueAsString(props));
            };
        }
    }
}
