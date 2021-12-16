package io.github.hylexus.jt.jt808.boot.config;

import io.github.hylexus.jt.jt808.boot.config.condition.ConditionalOnJt808BuiltinComponentsEnabled;
import io.github.hylexus.jt.jt808.boot.config.configuration.Jt808DispatcherHandlerAutoConfiguration;
import io.github.hylexus.jt.jt808.boot.config.configuration.Jt808NettyServerAutoConfiguration;
import io.github.hylexus.jt.jt808.boot.config.configuration.Jt808ServerComponentStatistics;
import io.github.hylexus.jt.jt808.boot.config.configuration.codec.Jt808CodecAutoConfiguration;
import io.github.hylexus.jt.jt808.boot.props.Jt808ServerProps;
import io.github.hylexus.jt.jt808.spec.Jt808CommandSender;
import io.github.hylexus.jt.jt808.spec.MsgTypeParser;
import io.github.hylexus.jt.jt808.spec.impl.BuiltinMsgTypeParser;
import io.github.hylexus.jt.jt808.spec.impl.DefaultJt808CommandSender;
import io.github.hylexus.jt.jt808.spec.session.Jt808SessionManager;
import io.github.hylexus.jt.jt808.support.annotation.codec.Jt808AnnotationBasedEncoder;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgEncoder;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.builtin.BuiltinTerminalAuthenticationMsgHandlerForDebugging;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.builtin.BuiltinTerminalRegisterMsgHandlerForDebugging;
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
    public MsgTypeParser msgTypeParser() {
        return new BuiltinMsgTypeParser();
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
    public BuiltinTerminalRegisterMsgHandlerForDebugging builtinJt808RequestHandlerForDebugging() {
        return new BuiltinTerminalRegisterMsgHandlerForDebugging();
    }

    @Bean
    @ConditionalOnJt808BuiltinComponentsEnabled
    public BuiltinTerminalAuthenticationMsgHandlerForDebugging builtinTerminalAuthenticationMsgHandlerForDebugging() {
        return new BuiltinTerminalAuthenticationMsgHandlerForDebugging();
    }

    @Bean
    @ConditionalOnProperty(prefix = "jt808", name = "print-component-statistics", havingValue = "true", matchIfMissing = true)
    public Jt808ServerComponentStatistics jt808ServerComponentStatistics(MsgTypeParser msgTypeParser) {
        return new Jt808ServerComponentStatistics(msgTypeParser);
    }
}
