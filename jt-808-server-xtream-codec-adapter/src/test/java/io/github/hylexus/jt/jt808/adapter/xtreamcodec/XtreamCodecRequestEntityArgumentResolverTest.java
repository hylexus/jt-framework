package io.github.hylexus.jt.jt808.adapter.xtreamcodec;

import io.github.hylexus.jt.jt808.adapter.xtreamcodec.entity.Message0100JtFramework;
import io.github.hylexus.jt.jt808.adapter.xtreamcodec.entity.Message0100XtreamCodec;
import io.github.hylexus.jt.jt808.spec.*;
import io.github.hylexus.jt.jt808.spec.impl.BuiltinJt808MsgTypeParser;
import io.github.hylexus.jt.jt808.spec.impl.DefaultJt808ProtocolVersionDetector;
import io.github.hylexus.jt.jt808.spec.impl.DefaultJt808ProtocolVersionDetectorRegistry;
import io.github.hylexus.jt.jt808.spec.impl.DefaultJt808ServerExchange;
import io.github.hylexus.jt.jt808.spec.session.Jt808Session;
import io.github.hylexus.jt.jt808.support.annotation.codec.Jt808AnnotationBasedDecoder;
import io.github.hylexus.jt.jt808.support.annotation.msg.DrivenBy;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.Jt808RequestBody;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgDecoder;
import io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808MsgBytesProcessor;
import io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808MsgDecoder;
import io.github.hylexus.jt.jt808.support.data.deserialize.DefaultJt808FieldDeserializerRegistry;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver.ArgumentContext;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.reflection.MethodParameter;
import io.github.hylexus.jt.jt808.support.exception.Jt808ArgumentResolveException;
import io.github.hylexus.jt.utils.JtAnnotationUtils;
import io.github.hylexus.xtream.codec.common.utils.XtreamBytes;
import io.github.hylexus.xtream.codec.core.EntityCodec;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class XtreamCodecRequestEntityArgumentResolverTest {
    Jt808MsgDecoder jt808MsgDecoder;
    XtreamCodecRequestEntityArgumentResolver resolver;

    @BeforeEach
    void setUp() {
        jt808MsgDecoder = new DefaultJt808MsgDecoder(
                new BuiltinJt808MsgTypeParser(),
                new DefaultJt808MsgBytesProcessor(ByteBufAllocator.DEFAULT),
                new DefaultJt808ProtocolVersionDetectorRegistry(new DefaultJt808ProtocolVersionDetector()),
                Jt808MsgEncryptionHandler.NO_OPS
        );
        resolver = new XtreamCodecRequestEntityArgumentResolver(
                EntityCodec.DEFAULT,
                new Jt808AnnotationBasedDecoder(new DefaultJt808FieldDeserializerRegistry(true))
        );
    }

    @Test
    void testSupportsParameter() {
        assertTrue(resolver.supportsParameter(mockMethodParameter(Message0100XtreamCodec.class)));
        assertTrue(resolver.supportsParameter(mockMethodParameter(Message0100JtFramework.class)));
        assertTrue(resolver.supportsParameter(mockMethodParameter(Object.class)));
    }

    @Test
    void testGenricTypeMissing() {
        final String hex = "0100405601000000000139123443290000000b0002696431323334353637383974797065313233343536373839303132333435363738393031323334353669643132333435363738393031323334353637383930313233343536373801b8ca4a2d31323334353951";
        final ByteBuf buffer = XtreamBytes.byteBufFromHexString(ByteBufAllocator.DEFAULT, hex);
        MutableJt808Request request = null;
        try {
            request = this.jt808MsgDecoder.decode(buffer);
            final ArgumentContext context = mockContext(request);
            final MethodParameter methodParameter = mockMethodParameter(Message0100XtreamCodec.class);
            when(methodParameter.getGenericType()).thenReturn(List.of());
            assertThrows(
                    Jt808ArgumentResolveException.class,
                    () -> resolver.resolveArgument(methodParameter, context),
                    () -> "Can not resolve GenericType on type [" + Jt808RequestEntity.class + "]"
            );
        } finally {
            requireNonNull(request).release();
            buffer.release();
            assertEquals(0, buffer.refCnt());
        }
    }

    @Test
    void testDrivenByXtreamCodec() {
        final String hex = "0100405601000000000139123443290000000b0002696431323334353637383974797065313233343536373839303132333435363738393031323334353669643132333435363738393031323334353637383930313233343536373801b8ca4a2d31323334353951";
        final ByteBuf buffer = XtreamBytes.byteBufFromHexString(ByteBufAllocator.DEFAULT, hex);
        MutableJt808Request request = null;
        try {
            request = this.jt808MsgDecoder.decode(buffer);
            final ArgumentContext context = mockContext(request);
            final MethodParameter methodParameter = mockMethodParameter(Message0100XtreamCodec.class);
            @SuppressWarnings("unchecked") final Jt808RequestEntity<Message0100XtreamCodec> requestEntity = (Jt808RequestEntity<Message0100XtreamCodec>) resolver.resolveArgument(methodParameter, context);
            final Message0100XtreamCodec decodedEntity = requestEntity.body();
            assertNotNull(decodedEntity);

            assertEquals(11, decodedEntity.provinceId);
            assertEquals(2, decodedEntity.cityId);
            assertEquals("id123456789", decodedEntity.manufacturerId);
            assertEquals("type12345678901234567890123456", decodedEntity.terminalType);
            assertEquals("id1234567890123456789012345678", decodedEntity.terminalId);
            assertEquals(1, decodedEntity.color);
            assertEquals("甘J-123459", decodedEntity.carIdentifier);
        } finally {
            requireNonNull(request).release();
            buffer.release();
            assertEquals(0, buffer.refCnt());
        }
    }

    @Test
    void testDrivenByJtFramework() {
        final String hex = "0100405601000000000139123443290000000b0002696431323334353637383974797065313233343536373839303132333435363738393031323334353669643132333435363738393031323334353637383930313233343536373801b8ca4a2d31323334353951";
        final ByteBuf buffer = XtreamBytes.byteBufFromHexString(ByteBufAllocator.DEFAULT, hex);
        MutableJt808Request request = null;
        try {
            request = this.jt808MsgDecoder.decode(buffer);
            final ArgumentContext context = mockContext(request);
            final MethodParameter methodParameter = mockMethodParameter(Message0100JtFramework.class);
            @SuppressWarnings("unchecked") final Jt808RequestEntity<Message0100JtFramework> requestEntity = (Jt808RequestEntity<Message0100JtFramework>) resolver.resolveArgument(methodParameter, context);
            final Message0100JtFramework decodedEntity = requestEntity.body();
            assertNotNull(decodedEntity);

            assertEquals(11, decodedEntity.provinceId);
            assertEquals(2, decodedEntity.cityId);
            assertEquals("id123456789", decodedEntity.manufacturerId);
            assertEquals("type12345678901234567890123456", decodedEntity.terminalType);
            assertEquals("id1234567890123456789012345678", decodedEntity.terminalId);
            assertEquals(1, decodedEntity.color);
            assertEquals("甘J-123459", decodedEntity.carIdentifier);
        } finally {
            requireNonNull(request).release();
            buffer.release();
            assertEquals(0, buffer.refCnt());
        }
    }

    private static MethodParameter mockMethodParameter(Class<?> entityType) {
        final DrivenBy drivenBy = Optional.ofNullable(JtAnnotationUtils.getMergedAnnotation(entityType, Jt808RequestBody.class))
                .map(Jt808RequestBody::drivenBy)
                .orElse(null);
        return spy(
                new MethodParameter()
                        .setIndex(0)
                        .setParameterType(Jt808RequestEntity.class)
                        .setGenericType(List.of(entityType))
                        .setDrivenBy(drivenBy)
        );
    }

    private static @NonNull ArgumentContext mockContext(MutableJt808Request request) {
        final Jt808ServerExchange exchange = new DefaultJt808ServerExchange(
                spy(request),
                mock(Jt808Response.class),
                mock(Jt808Session.class)
        );
        return ArgumentContext.of(exchange, null);
    }
}
