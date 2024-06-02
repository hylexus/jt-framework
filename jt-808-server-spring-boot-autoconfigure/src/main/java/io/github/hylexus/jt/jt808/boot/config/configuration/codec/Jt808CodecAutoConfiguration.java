package io.github.hylexus.jt.jt808.boot.config.configuration.codec;

import io.github.hylexus.jt.jt808.spec.Jt808MsgEncryptionHandler;
import io.github.hylexus.jt.jt808.spec.Jt808MsgTypeParser;
import io.github.hylexus.jt.jt808.spec.Jt808ProtocolVersionDetector;
import io.github.hylexus.jt.jt808.spec.Jt808ProtocolVersionDetectorRegistry;
import io.github.hylexus.jt.jt808.spec.impl.BuiltinTerminalRegisterJt808ProtocolVersionDetector;
import io.github.hylexus.jt.jt808.spec.impl.DefaultJt808ProtocolVersionDetector;
import io.github.hylexus.jt.jt808.spec.impl.DefaultJt808ProtocolVersionDetectorRegistry;
import io.github.hylexus.jt.jt808.support.annotation.codec.Jt808AnnotationBasedDecoder;
import io.github.hylexus.jt.jt808.support.annotation.codec.Jt808AnnotationBasedEncoder;
import io.github.hylexus.jt.jt808.support.annotation.codec.SimpleJt808MsgDecoder;
import io.github.hylexus.jt.jt808.support.codec.*;
import io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808MsgBytesProcessor;
import io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808MsgDecoder;
import io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808MsgEncoder;
import io.github.hylexus.jt.jt808.support.data.deserialize.DefaultJt808FieldDeserializerRegistry;
import io.github.hylexus.jt.jt808.support.data.deserialize.Jt808FieldDeserializerRegistry;
import io.github.hylexus.jt.jt808.support.data.serializer.DefaultJt808FieldSerializerRegistry;
import io.github.hylexus.jt.jt808.support.data.serializer.Jt808FieldSerializerRegistry;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.Comparator;

/**
 * @author hylexus
 */
public class Jt808CodecAutoConfiguration {

    @Bean
    public Jt808ProtocolVersionDetector builtinTerminalRegisterJt808ProtocolVersionDetector() {
        return new BuiltinTerminalRegisterJt808ProtocolVersionDetector();
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt808ProtocolVersionDetectorRegistry jt808ProtocolVersionDetectorRegistry(ObjectProvider<Jt808ProtocolVersionDetector> versionDetectors) {
        final Jt808ProtocolVersionDetectorRegistry registry = new DefaultJt808ProtocolVersionDetectorRegistry(new DefaultJt808ProtocolVersionDetector());

        versionDetectors.stream()
                .sorted(Comparator.comparing(Jt808ProtocolVersionDetector::getOrder))
                .forEach(detector -> detector.getSupportedMsgTypes()
                        .forEach(msgId -> registry.register(msgId, detector)));
        return registry;
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt808MsgBytesProcessor jt808MsgBytesProcessor() {
        return new DefaultJt808MsgBytesProcessor(ByteBufAllocator.DEFAULT);
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt808MsgEncryptionHandler jt808MsgEncryptionHandler() {
        return Jt808MsgEncryptionHandler.NO_OPS;
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt808MsgDecoder jt808MsgDecoder(
            Jt808MsgTypeParser msgTypeParser,
            Jt808MsgBytesProcessor msgBytesProcessor,
            Jt808ProtocolVersionDetectorRegistry versionDetectorRegistry,
            Jt808MsgEncryptionHandler encryptionHandler) {

        return new DefaultJt808MsgDecoder(msgTypeParser, msgBytesProcessor, versionDetectorRegistry, encryptionHandler);
    }

    @Bean
    @ConditionalOnMissingBean
    public Jt808MsgEncoder jt808MsgEncoder(
            Jt808MsgBytesProcessor msgBytesProcessor,
            Jt808ResponseSubPackageEventListener subPackageEventListener,
            Jt808ResponseSubPackageStorage subPackageStorage,
            Jt808MsgEncryptionHandler encryptionHandler) {
        return new DefaultJt808MsgEncoder(PooledByteBufAllocator.DEFAULT, msgBytesProcessor, subPackageEventListener, subPackageStorage, encryptionHandler);
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

        final Jt808FieldSerializerRegistry registry = new DefaultJt808FieldSerializerRegistry(true);
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

    @Bean
    @ConditionalOnMissingBean
    public SimpleJt808MsgDecoder simpleJt808MsgDecoder(Jt808AnnotationBasedDecoder delegate) {
        return new SimpleJt808MsgDecoder(delegate);
    }
}
