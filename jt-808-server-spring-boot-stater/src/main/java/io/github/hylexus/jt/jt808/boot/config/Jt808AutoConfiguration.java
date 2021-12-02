package io.github.hylexus.jt.jt808.boot.config;

import io.github.hylexus.jt.jt808.boot.config.configuration.Jt808DispatcherHandlerAutoConfiguration;
import io.github.hylexus.jt.jt808.boot.config.configuration.Jt808NettyServerAutoConfiguration;
import io.github.hylexus.jt.jt808.boot.props.Jt808ServerProps;
import io.github.hylexus.jt.jt808.response.Jt808CommandSender;
import io.github.hylexus.jt.jt808.response.impl.DefaultJt808CommandSender;
import io.github.hylexus.jt.jt808.session.Jt808SessionManager;
import io.github.hylexus.jt.jt808.support.annotation.codec.Jt808AnnotationBasedEncoder;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgBytesProcessor;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgDecoder;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgEncoder;
import io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808MsgBytesProcessor;
import io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808MsgDecoder;
import io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808MsgEncoder;
import io.github.hylexus.jt.jt808.support.converter.BuiltinMsgTypeParser;
import io.github.hylexus.jt.jt808.support.converter.MsgTypeParser;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * @author hylexus
 */
@Import({
        Jt808DispatcherHandlerAutoConfiguration.class,
        Jt808NettyServerAutoConfiguration.class,
})
@EnableConfigurationProperties({Jt808ServerProps.class})
public class Jt808AutoConfiguration {

    @Bean
    public MsgTypeParser msgTypeParser() {
        return new BuiltinMsgTypeParser();
    }

    @Bean
    public Jt808MsgBytesProcessor jt808MsgBytesProcessor() {
        return new DefaultJt808MsgBytesProcessor();
    }

    @Bean
    public Jt808MsgDecoder jt808MsgDecoder(MsgTypeParser msgTypeParser, Jt808MsgBytesProcessor msgBytesProcessor) {
        return new DefaultJt808MsgDecoder(msgTypeParser, msgBytesProcessor);
    }

    @Bean
    public Jt808MsgEncoder jt808MsgEncoder(Jt808MsgBytesProcessor msgBytesProcessor) {
        return new DefaultJt808MsgEncoder(msgBytesProcessor);
    }

    @Bean
    public Jt808AnnotationBasedEncoder jt808AnnotationBasedEncoder() {
        return new Jt808AnnotationBasedEncoder();
    }

    @Bean
    public Jt808CommandSender jt808CommandSender(
            Jt808SessionManager sessionManager, Jt808MsgEncoder encoder,
            Jt808AnnotationBasedEncoder annotationBasedEncoder) {
        return new DefaultJt808CommandSender(sessionManager, encoder, annotationBasedEncoder);
    }

}
