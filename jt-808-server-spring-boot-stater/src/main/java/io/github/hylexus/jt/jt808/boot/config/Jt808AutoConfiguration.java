package io.github.hylexus.jt.jt808.boot.config;

import io.github.hylexus.jt.jt808.boot.config.condition.ConditionalOnJt808BuiltinComponentsEnabled;
import io.github.hylexus.jt.jt808.boot.config.configuration.Jt808DispatcherHandlerAutoConfiguration;
import io.github.hylexus.jt.jt808.boot.config.configuration.Jt808NettyServerAutoConfiguration;
import io.github.hylexus.jt.jt808.boot.config.configuration.Jt808ServerComponentStatistics;
import io.github.hylexus.jt.jt808.boot.config.configuration.codec.Jt808CodecAutoConfiguration;
import io.github.hylexus.jt.jt808.boot.props.Jt808ServerProps;
import io.github.hylexus.jt.jt808.spec.Jt808CommandSender;
import io.github.hylexus.jt.jt808.spec.Jt808MsgTypeParser;
import io.github.hylexus.jt.jt808.spec.impl.BuiltinJt808MsgTypeParser;
import io.github.hylexus.jt.jt808.spec.impl.DefaultJt808CommandSender;
import io.github.hylexus.jt.jt808.spec.session.Jt808SessionManager;
import io.github.hylexus.jt.jt808.support.annotation.codec.Jt808AnnotationBasedEncoder;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgEncoder;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.builtin.BuiltinCommonHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.builtin.BuiltinTerminalAuthenticationMsgHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.builtin.BuiltinTerminalRegisterMsgHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * @author hylexus
 */
@Import({
        Jt808CodecAutoConfiguration.class,
        Jt808DispatcherHandlerAutoConfiguration.class,
        Jt808NettyServerAutoConfiguration.class,
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
    @ConditionalOnJt808BuiltinComponentsEnabled
    public BuiltinTerminalRegisterMsgHandler builtinJt808RequestHandlerForDebugging() {
        return new BuiltinTerminalRegisterMsgHandler();
    }

    @Bean
    @ConditionalOnJt808BuiltinComponentsEnabled
    public BuiltinTerminalAuthenticationMsgHandler builtinTerminalAuthenticationMsgHandlerForDebugging() {
        return new BuiltinTerminalAuthenticationMsgHandler();
    }

    @Bean
    @ConditionalOnJt808BuiltinComponentsEnabled
    public BuiltinCommonHandler builtinCommonHandler() {
        return new BuiltinCommonHandler();
    }

    @Bean
    @ConditionalOnProperty(prefix = "jt808.built-components.component-statistics", name = "enabled", havingValue = "true", matchIfMissing = true)
    public Jt808ServerComponentStatistics jt808ServerComponentStatistics() {
        return new Jt808ServerComponentStatistics();
    }
}
