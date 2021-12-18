package io.github.hylexus.jt.jt808.boot.config.configuration.codec;

import io.github.hylexus.jt.jt808.spec.Jt808MsgTypeParser;
import io.github.hylexus.jt.jt808.support.annotation.codec.Jt808AnnotationBasedDecoder;
import io.github.hylexus.jt.jt808.support.annotation.codec.Jt808AnnotationBasedEncoder;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgBytesProcessor;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgDecoder;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgEncoder;
import io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808MsgBytesProcessor;
import io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808MsgDecoder;
import io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808MsgEncoder;
import io.github.hylexus.jt.jt808.support.data.deserialize.DefaultJt808FieldDeserializerRegistry;
import io.github.hylexus.jt.jt808.support.data.deserialize.Jt808FieldDeserializerRegistry;
import io.github.hylexus.jt.jt808.support.data.serializer.DefaulJt808FieldSerializerRegistry;
import io.github.hylexus.jt.jt808.support.data.serializer.Jt808FieldSerializerRegistry;
import io.netty.buffer.ByteBufAllocator;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author hylexus
 */
public class Jt808CodecAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public Jt808MsgBytesProcessor jt808MsgBytesProcessor() {
        return new DefaultJt808MsgBytesProcessor(ByteBufAllocator.DEFAULT);
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt808MsgDecoder jt808MsgDecoder(Jt808MsgTypeParser msgTypeParser, Jt808MsgBytesProcessor msgBytesProcessor) {
        return new DefaultJt808MsgDecoder(msgTypeParser, msgBytesProcessor);
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt808MsgEncoder jt808MsgEncoder(Jt808MsgBytesProcessor msgBytesProcessor) {
        return new DefaultJt808MsgEncoder(msgBytesProcessor);
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt808FieldDeserializerRegistry jt808FieldDeserializerRegistry(
            ObjectProvider<Jt808FieldDeserializerRegistry.Jt808FieldDeserializerRegistryCustomizer> customizers) {

        final Jt808FieldDeserializerRegistry registry = new DefaultJt808FieldDeserializerRegistry(true);
        customizers.orderedStream().forEach(e -> e.customize(registry));
        return registry;
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt808FieldSerializerRegistry jt808FieldSerializerRegistry(
            ObjectProvider<Jt808FieldSerializerRegistry.Jt808FieldSerializerRegistryCustomizer> customizers) {

        final Jt808FieldSerializerRegistry registry = new DefaulJt808FieldSerializerRegistry(true);
        customizers.orderedStream().forEach(e -> e.customize(registry));
        return registry;
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt808AnnotationBasedDecoder jt808AnnotationBasedDecoder(Jt808FieldDeserializerRegistry fieldDeserializerRegistry) {
        return new Jt808AnnotationBasedDecoder(fieldDeserializerRegistry);
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt808AnnotationBasedEncoder jt808AnnotationBasedEncoder(Jt808FieldSerializerRegistry fieldSerializerRegistry) {
        return new Jt808AnnotationBasedEncoder(fieldSerializerRegistry);
    }
}
