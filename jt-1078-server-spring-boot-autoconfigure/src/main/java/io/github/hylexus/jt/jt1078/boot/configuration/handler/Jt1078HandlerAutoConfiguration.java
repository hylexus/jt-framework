package io.github.hylexus.jt.jt1078.boot.configuration.handler;

import io.github.hylexus.jt.jt1078.boot.condition.ConditionalOnJt1078RequestSubPackageCombinerEnabled;
import io.github.hylexus.jt.jt1078.boot.props.Jt1078ServerProps;
import io.github.hylexus.jt.jt1078.boot.props.subpackage.RequestSubPackageCombinerProps;
import io.github.hylexus.jt.jt1078.spec.Jt1078PublisherInternal;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionManager;
import io.github.hylexus.jt.jt1078.spec.Jt1078TerminalIdConverter;
import io.github.hylexus.jt.jt1078.spec.impl.DefaultJt1078TerminalIdConverter;
import io.github.hylexus.jt.jt1078.spec.impl.subscription.DefaultCollectorFactory;
import io.github.hylexus.jt.jt1078.spec.impl.subscription.DefaultJt1078Publisher;
import io.github.hylexus.jt.jt1078.spec.impl.subscription.Jt1078ChannelCollectorFactory;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078MsgDecoder;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078RequestSubPackageCombiner;
import io.github.hylexus.jt.jt1078.support.codec.impl.CaffeineJt1078RequestSubPackageCombiner;
import io.github.hylexus.jt.jt1078.support.dispatcher.Jt1078RequestHandler;
import io.github.hylexus.jt.jt1078.support.dispatcher.Jt1078RequestMsgDispatcher;
import io.github.hylexus.jt.jt1078.support.dispatcher.Jt1078RequestPreprocessor;
import io.github.hylexus.jt.jt1078.support.dispatcher.impl.DefaultJt1078RequestMsgDispatcher;
import io.github.hylexus.jt.jt1078.support.dispatcher.impl.DefaultPublisherBasedJt1078RequestHandler;
import io.github.hylexus.jt.jt1078.support.netty.Jt1078DispatcherChannelHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.List;

public class Jt1078HandlerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public Jt1078ChannelCollectorFactory jt1078ChannelCollectorFactory() {
        return new DefaultCollectorFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt1078TerminalIdConverter jt1078TerminalIdConverter() {
        return new DefaultJt1078TerminalIdConverter();
    }

    @Bean(destroyMethod = "close")
    public Jt1078PublisherInternal internalJt1078Publisher(Jt1078ChannelCollectorFactory collectorFactory, Jt1078TerminalIdConverter jt1078TerminalIdConverter) {
        return new DefaultJt1078Publisher(collectorFactory, jt1078TerminalIdConverter);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnJt1078RequestSubPackageCombinerEnabled(type = RequestSubPackageCombinerProps.Type.CAFFEINE)
    public Jt1078RequestSubPackageCombiner jt1078RequestSubPackageCombiner(Jt1078ServerProps serverProps) {
        return new CaffeineJt1078RequestSubPackageCombiner(serverProps.getRequestSubPackageCombiner().getCaffeine());
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnJt1078RequestSubPackageCombinerEnabled(type = RequestSubPackageCombinerProps.Type.NONE)
    public Jt1078RequestSubPackageCombiner noOpsjt1078RequestSubPackageCombiner() {
        return Jt1078RequestSubPackageCombiner.NO_OPS;
    }

    @Bean
    public Jt1078RequestHandler defaultSubscriberBasedJt1078RequestHandler(Jt1078PublisherInternal jt1078PublisherManager) {
        return new DefaultPublisherBasedJt1078RequestHandler(jt1078PublisherManager);
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt1078RequestMsgDispatcher jt1078RequestMsgDispatcher(List<Jt1078RequestHandler> handlers, Jt1078RequestSubPackageCombiner combiner) {
        return new DefaultJt1078RequestMsgDispatcher(handlers, combiner);
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt1078RequestPreprocessor jt1078RequestPreprocessor(
            Jt1078SessionManager jt1078SessionManager,
            Jt1078MsgDecoder jt1078MsgDecoder,
            Jt1078RequestMsgDispatcher jt1078RequestMsgDispatcher) {
        return new Jt1078RequestPreprocessor(jt1078SessionManager, jt1078MsgDecoder, jt1078RequestMsgDispatcher);
    }

    @Bean
    @ConditionalOnMissingBean(Jt1078DispatcherChannelHandler.class)
    public Jt1078DispatcherChannelHandler jt1078DispatcherChannelHandler(
            Jt1078SessionManager jt1078SessionManager,
            Jt1078RequestPreprocessor preprocessor) {
        return new Jt1078DispatcherChannelHandler(jt1078SessionManager, preprocessor);
    }
}
