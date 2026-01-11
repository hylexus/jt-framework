package io.github.hylexus.jt.jt808.adapter.xtreamcodec.boot2;

import io.github.hylexus.jt.jt808.adapter.xtreamcodec.XtreamCodecJt808ResponseBodyHandler;
import io.github.hylexus.jt.jt808.adapter.xtreamcodec.XtreamCodecRequestBodyArgumentResolver;
import io.github.hylexus.jt.jt808.adapter.xtreamcodec.XtreamCodecRequestEntityArgumentResolver;
import io.github.hylexus.jt.jt808.adapter.xtreamcodec.properties.XtreamCodecProperties;
import io.github.hylexus.jt.jt808.boot.config.configuration.dispatcher.HandlerResultHandlerAutoConfiguration;
import io.github.hylexus.jt.jt808.support.annotation.codec.Jt808AnnotationBasedDecoder;
import io.github.hylexus.jt.jt808.support.annotation.codec.Jt808AnnotationBasedEncoder;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgEncoder;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerResultHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.result.Jt808ResponseBodyHandlerResultHandler;
import io.github.hylexus.xtream.codec.base.expression.AviatorXtreamExpressionEngine;
import io.github.hylexus.xtream.codec.base.expression.MvelXtreamExpressionEngine;
import io.github.hylexus.xtream.codec.base.expression.SpelXtreamExpressionEngine;
import io.github.hylexus.xtream.codec.base.expression.XtreamExpressionEngine;
import io.github.hylexus.xtream.codec.core.*;
import io.github.hylexus.xtream.codec.core.impl.DefaultFieldCodecRegistry;
import io.github.hylexus.xtream.codec.core.impl.DefaultXtreamExpressionFactory;
import io.github.hylexus.xtream.codec.core.impl.SimpleBeanMetadataRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@ConditionalOnClass(EntityCodec.class)
@EnableConfigurationProperties({
        XtreamCodecProperties.class,
})
@Import({
        XtreamCodecAutoConfiguration.XtreamCodecConfiguration.class,
        XtreamCodecAutoConfiguration.AdapterConfiguration.class,
})
public class XtreamCodecAutoConfiguration {

    @Configuration(proxyBeanMethods = false)
    static class XtreamCodecConfiguration {
        @Bean
        @ConditionalOnMissingBean
        FieldCodecRegistry fieldCodecRegistry() {
            return new DefaultFieldCodecRegistry();
        }

        @Bean
        @ConditionalOnMissingBean
        XtreamCacheableClassPredicate xtreamCacheableClassPredicate() {
            return new XtreamCacheableClassPredicate.Default();
        }

        @Bean
        @ConditionalOnMissingBean
        @ConditionalOnExpression("'${xtream.codec.expression.type:}'.toLowerCase() != 'custom'")
        @SuppressWarnings("UnstableApiUsage")
        XtreamExpressionFactory xtreamExpressionFactory(XtreamCodecProperties properties) {
            final XtreamExpressionEngine expressionEngine = switch (properties.getExpression().getType()) {
                case SPEL -> new SpelXtreamExpressionEngine();
                case MVEL -> new MvelXtreamExpressionEngine();
                case AVIATOR -> new AviatorXtreamExpressionEngine();
                // 自定义类型不应该走到这里
                case CUSTOM -> throw new IllegalStateException("Invalid expression type: " + properties.getExpression().getType());
                case null -> throw new IllegalArgumentException("Invalid expression type: " + properties.getExpression().getType());
            };
            return new DefaultXtreamExpressionFactory(expressionEngine);
        }

        @Bean
        @ConditionalOnMissingBean
        @SuppressWarnings("UnstableApiUsage")
        BeanMetadataRegistry beanMetadataRegistry(FieldCodecRegistry fieldCodecRegistry, XtreamCacheableClassPredicate cacheableClassPredicate, XtreamExpressionFactory expressionFactory) {
            return new SimpleBeanMetadataRegistry(fieldCodecRegistry, cacheableClassPredicate, expressionFactory);
        }

        @Bean
        @ConditionalOnMissingBean
        EntityCodec entityCodec(BeanMetadataRegistry registry) {
            return new EntityCodec(registry);
        }

    }

    @Configuration(proxyBeanMethods = false)
    static class AdapterConfiguration {

        @Bean
        XtreamCodecRequestEntityArgumentResolver xtreamCodecRequestEntityArgumentResolver(
                EntityCodec entityCodec,
                Jt808AnnotationBasedDecoder decoder) {
            return new XtreamCodecRequestEntityArgumentResolver(entityCodec, decoder);
        }

        @Bean
        XtreamCodecRequestBodyArgumentResolver xtreamCodecRequestBodyArgumentResolver(
                EntityCodec entityCodec,
                Jt808AnnotationBasedDecoder decoder) {
            return new XtreamCodecRequestBodyArgumentResolver(entityCodec, decoder);
        }

        @Bean
        Jt808HandlerResultHandler xtreamCodecJt808ResponseBodyHandler(
                Jt808MsgEncoder encoder,
                Jt808AnnotationBasedEncoder annotationBasedEncoder,
                EntityCodec entityCodec,
                @Qualifier(HandlerResultHandlerAutoConfiguration.JT_808_HANDLER_RESULT_HANDLER_BEAN_NAME) Jt808HandlerResultHandler fallbackHandler) {
            return new XtreamCodecJt808ResponseBodyHandler(encoder, annotationBasedEncoder, entityCodec, (Jt808ResponseBodyHandlerResultHandler) fallbackHandler);
        }
    }

}
