package io.github.hylexus.jt.jt808.boot.config.configuration.dispatcher;

import io.github.hylexus.jt.jt808.support.annotation.codec.Jt808AnnotationBasedEncoder;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgEncoder;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerResultHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.result.Jt808ResponseBodyHandlerResultHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.result.Jt808ResponseHandlerResultHandler;
import org.springframework.context.annotation.Bean;

/**
 * @author hylexus
 */
public class HandlerResultHandlerAutoConfiguration {

    @Bean
    public Jt808HandlerResultHandler jt808ResponseHandlerResultHandler(Jt808MsgEncoder encoder) {
        return new Jt808ResponseHandlerResultHandler(encoder);
    }

    @Bean
    public Jt808HandlerResultHandler jt808ResponseMsgBodyHandlerResultHandler(Jt808MsgEncoder encoder, Jt808AnnotationBasedEncoder annotationBasedEncoder) {
        return new Jt808ResponseBodyHandlerResultHandler(annotationBasedEncoder, encoder);
    }

}