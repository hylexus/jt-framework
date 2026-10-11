package io.github.hylexus.jt.jt808.support.data.deserialize.extension;

import io.github.hylexus.jt.jt808.spec.Jt808Request;
import io.github.hylexus.jt.jt808.support.annotation.codec.Jt808AnnotationBasedDecoder;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestFieldAlias;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.extensions.KeyValueMapping;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.extensions.ValueDescriptor;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.RequestMsgConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.deserialize.DefaultJt808FieldDeserializerRegistry;
import io.github.hylexus.jt.jt808.support.data.deserialize.Jt808FieldDeserializer;
import io.github.hylexus.jt.jt808.support.data.meta.JavaBeanFieldMetadata;
import io.github.hylexus.jt.jt808.support.data.type.byteseq.ByteArrayContainer;
import io.github.hylexus.jt.jt808.support.utils.JavaBeanMetadataUtils;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.IntConsumer;

import static io.github.hylexus.jt.jt808.support.data.deserialize.extension.ExtendedJt808FieldDeserializerLocationExtraItemTest.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExtendedJt808FieldDeserializerLocationExtraItemConcurrencyTest {

    private static final String LOCATION_ITEMS = "01040000005d020201f1310109fe02aabb";

    @Test
    void concurrentFirstAndLaterRequestsKeepTypesDefaultsAndClassIsolation() throws Exception {
        for (int round = 0; round < 3; round++) {
            final Jt808AnnotationBasedDecoder decoder = decoder();
            final IntConsumer request = index -> {
                switch (index % 4) {
                    case 0:
                        assertLocationItems(decode(decoder, new TypedMessage(), LOCATION_ITEMS).getItems());
                        break;
                    case 1:
                        final Map<Integer, Object> raw = decode(decoder, new RawMessage(), LOCATION_ITEMS).getItems();
                        assertArrayEquals(new byte[]{0, 0, 0, 93}, (byte[]) raw.get(1));
                        assertArrayEquals(new byte[]{(byte) 0xaa, (byte) 0xbb}, (byte[]) raw.get(0xfe));
                        break;
                    case 2:
                        final Map<Integer, Object> implicit = decode(decoder, new EmptyMappingMessage(), LOCATION_ITEMS).getItems();
                        assertArrayEquals(new byte[]{0, 0, 0, 93}, ((ByteArrayContainer) implicit.get(1)).value());
                        break;
                    default:
                        final Map<Integer, Object> explicit = decode(decoder, new EmptyRawMappingMessage(), LOCATION_ITEMS).getItems();
                        assertArrayEquals(new byte[]{0, 0, 0, 93}, (byte[]) explicit.get(1));
                }
            };
            runConcurrently(request);
            runConcurrently(request);
        }
    }

    @Test
    void concurrentFieldsInTheSameMessageRemainIsolated() throws Exception {
        final Jt808AnnotationBasedDecoder decoder = decoder();
        runConcurrently(index -> {
            final TwoFieldsMessage message = decode(decoder, new TwoFieldsMessage(), "310109310108");
            assertEquals(Integer.class, message.getFirst().get(0x31).getClass());
            assertEquals(9, message.getFirst().get(0x31));
            assertEquals(Short.class, message.getSecond().get(0x31).getClass());
            assertEquals((short) 8, message.getSecond().get(0x31));
        });
    }

    @Test
    void waitsForAnExplicitMappingThatIsStillBeingPrepared() throws Exception {
        assertCompletePublication(false);
    }

    @Test
    void waitsForTheDeclaredDefaultThatIsStillBeingPrepared() throws Exception {
        assertCompletePublication(true);
    }

    private static void assertCompletePublication(boolean pauseDefault) throws Exception {
        final CountDownLatch building = new CountDownLatch(1);
        final CountDownLatch releaseBuilder = new CountDownLatch(1);
        final CountDownLatch readerEntered = new CountDownLatch(1);
        final RequestFieldAlias.LocationMsgExtraItemMapping actual = actualAnnotation();
        final RequestFieldAlias.LocationMsgExtraItemMapping annotation = mock(RequestFieldAlias.LocationMsgExtraItemMapping.class);
        final KeyValueMapping[] mappings = actual.keyValueMappings().clone();
        if (pauseDefault) {
            final ValueDescriptor descriptor = pausedDescriptor(actual.defaultKeyValueMapping(), building, releaseBuilder);
            when(annotation.defaultKeyValueMapping()).thenReturn(descriptor);
        } else {
            final KeyValueMapping second = mock(KeyValueMapping.class);
            final ValueDescriptor descriptor = pausedDescriptor(mappings[1].value(), building, releaseBuilder);
            when(second.key()).thenReturn(mappings[1].key());
            when(second.value()).thenReturn(descriptor);
            mappings[1] = second;
            when(annotation.defaultKeyValueMapping()).thenReturn(actual.defaultKeyValueMapping());
        }
        when(annotation.keyValueMappings()).thenReturn(mappings);

        final Jt808AnnotationBasedDecoder decoder = decoder();
        final JavaBeanFieldMetadata metadata = metadata(annotation);
        final Jt808FieldDeserializer.Context builderContext = new Jt808FieldDeserializer.DefaultInternalDecoderContext(metadata, decoder, null);
        final Jt808FieldDeserializer.Context readerContext = mock(Jt808FieldDeserializer.Context.class);
        when(readerContext.delegate()).thenReturn(decoder);
        when(readerContext.fieldMetadata()).thenAnswer(invocation -> {
            readerEntered.countDown();
            return metadata;
        });

        final ExtendedJt808FieldDeserializerLocationExtraItem deserializer = new ExtendedJt808FieldDeserializerLocationExtraItem();
        final ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            final Future<Map<Integer, Object>> first = executor.submit(() -> read(deserializer, builderContext, "01040000005d"));
            await(building);
            final Future<Map<Integer, Object>> second = executor.submit(() -> read(deserializer, readerContext,
                    pauseDefault ? "fe02aabb" : "020201f1"));
            await(readerEntered);
            // A ready request must wait for complete metadata, not return a fallback from a half-built map.
            assertThrows(TimeoutException.class, () -> second.get(200, TimeUnit.MILLISECONDS));
            releaseBuilder.countDown();
            assertEquals(93L, first.get(10, TimeUnit.SECONDS).get(1));
            final Map<Integer, Object> result = second.get(10, TimeUnit.SECONDS);
            if (pauseDefault) {
                assertTrue(result.get(0xfe) instanceof ByteArrayContainer);
                assertArrayEquals(new byte[]{(byte) 0xaa, (byte) 0xbb}, ((ByteArrayContainer) result.get(0xfe)).value());
            } else {
                assertEquals(Integer.class, result.get(2).getClass());
                assertEquals(497, result.get(2));
            }
        } finally {
            releaseBuilder.countDown();
            shutdown(executor);
        }
    }

    @Test
    void failedPreparationIsNotPublishedAndAWaitingRequestCanRetry() throws Exception {
        final CountDownLatch building = new CountDownLatch(1);
        final CountDownLatch releaseBuilder = new CountDownLatch(1);
        final CountDownLatch readerEntered = new CountDownLatch(1);
        final IllegalArgumentException failure = new IllegalArgumentException("metadata preparation failure");
        final RequestFieldAlias.LocationMsgExtraItemMapping actual = actualAnnotation();
        final KeyValueMapping[] mappings = actual.keyValueMappings().clone();
        final ValueDescriptor descriptor = mock(ValueDescriptor.class);
        final AtomicInteger attempts = new AtomicInteger();
        when(descriptor.source()).thenAnswer(invocation -> {
            if (attempts.getAndIncrement() == 0) {
                building.countDown();
                await(releaseBuilder);
                throw failure;
            }
            return MsgDataType.WORD;
        });
        when(descriptor.target()).thenAnswer(invocation -> Integer.class);
        final KeyValueMapping second = mock(KeyValueMapping.class);
        when(second.key()).thenReturn(2);
        when(second.value()).thenReturn(descriptor);
        mappings[1] = second;
        final RequestFieldAlias.LocationMsgExtraItemMapping annotation = mock(RequestFieldAlias.LocationMsgExtraItemMapping.class);
        when(annotation.keyValueMappings()).thenReturn(mappings);
        when(annotation.defaultKeyValueMapping()).thenReturn(actual.defaultKeyValueMapping());

        final Jt808AnnotationBasedDecoder decoder = decoder();
        final JavaBeanFieldMetadata metadata = metadata(annotation);
        final Jt808FieldDeserializer.Context context = new Jt808FieldDeserializer.DefaultInternalDecoderContext(metadata, decoder, null);
        final Jt808FieldDeserializer.Context readerContext = mock(Jt808FieldDeserializer.Context.class);
        when(readerContext.delegate()).thenReturn(decoder);
        when(readerContext.fieldMetadata()).thenAnswer(invocation -> {
            readerEntered.countDown();
            return metadata;
        });
        final ExtendedJt808FieldDeserializerLocationExtraItem deserializer = new ExtendedJt808FieldDeserializerLocationExtraItem();
        final ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            final Future<Map<Integer, Object>> first = executor.submit(() -> read(deserializer, context, "01040000005d"));
            await(building);
            final Future<Map<Integer, Object>> secondResult = executor.submit(() -> read(deserializer, readerContext, "020201f1"));
            await(readerEntered);
            assertThrows(TimeoutException.class, () -> secondResult.get(200, TimeUnit.MILLISECONDS));
            releaseBuilder.countDown();
            assertSame(failure, assertThrows(ExecutionException.class, () -> first.get(10, TimeUnit.SECONDS)).getCause());
            final Map<Integer, Object> result = secondResult.get(10, TimeUnit.SECONDS);
            assertEquals(Integer.class, result.get(2).getClass());
            assertEquals(497, result.get(2));
            assertLocationItems(read(deserializer, context, LOCATION_ITEMS));
            assertEquals(2, attempts.get());
        } finally {
            releaseBuilder.countDown();
            shutdown(executor);
        }
    }

    @Test
    void noItemsDoNotRequirePreparingMetadata() {
        final RequestFieldAlias.LocationMsgExtraItemMapping annotation = mock(RequestFieldAlias.LocationMsgExtraItemMapping.class);
        final Jt808FieldDeserializer.Context context = new Jt808FieldDeserializer.DefaultInternalDecoderContext(metadata(annotation), decoder(), null);
        assertTrue(read(new ExtendedJt808FieldDeserializerLocationExtraItem(), context, "").isEmpty());
        verifyNoInteractions(annotation);
    }

    @Test
    void releasesBasicConverterContentOnSuccessAndFailureWithoutReleasingInput() {
        for (boolean fail : new boolean[]{false, true}) {
            final AtomicReference<ByteBuf> content = new AtomicReference<>();
            final IllegalArgumentException failure = new IllegalArgumentException("conversion failure");
            final DefaultJt808FieldDeserializerRegistry registry = new DefaultJt808FieldDeserializerRegistry(true);
            registry.registerConverter(new Jt808FieldDeserializer<Long>() {
                @Override
                public Set<RequestMsgConvertibleMetadata> getConvertibleTypes() {
                    return Collections.singleton(new RequestMsgConvertibleMetadata(MsgDataType.DWORD, Long.class));
                }

                @Override
                public Long deserialize(ByteBuf body, MsgDataType type, int start, int length) {
                    content.set(body);
                    if (fail) {
                        throw failure;
                    }
                    return body.readUnsignedInt();
                }
            });
            final Jt808AnnotationBasedDecoder decoder = new Jt808AnnotationBasedDecoder(registry);
            final Jt808FieldDeserializer.Context context = new Jt808FieldDeserializer.DefaultInternalDecoderContext(metadata(actualAnnotation()), decoder, null);
            final ExtendedJt808FieldDeserializerLocationExtraItem deserializer = new ExtendedJt808FieldDeserializerLocationExtraItem();
            if (fail) {
                assertSame(failure, assertThrows(IllegalArgumentException.class, () -> read(deserializer, context, "01040000005d")));
            } else {
                assertEquals(93L, read(deserializer, context, "01040000005d").get(1));
            }
            assertNotNull(content.get());
            assertEquals(0, content.get().refCnt());
        }
    }

    @Test
    void releasesNestedContentOnSuccessAndFailure() {
        for (boolean fail : new boolean[]{false, true}) {
            final AtomicReference<ByteBuf> content = new AtomicReference<>();
            final IllegalArgumentException failure = new IllegalArgumentException("nested conversion failure");
            final Jt808AnnotationBasedDecoder decoder = new Jt808AnnotationBasedDecoder(new DefaultJt808FieldDeserializerRegistry(true)) {
                @Override
                public Object decode(Class<?> cls, Object instance, ByteBuf body, Jt808Request request) {
                    content.set(body);
                    if (fail) {
                        throw failure;
                    }
                    return super.decode(cls, instance, body, request);
                }
            };
            final JavaBeanFieldMetadata metadata = JavaBeanMetadataUtils.getBeanMetadata(NestedMessage.class).getFieldMapping().get("items");
            final Jt808FieldDeserializer.Context context = new Jt808FieldDeserializer.DefaultInternalDecoderContext(metadata, decoder, null);
            final ExtendedJt808FieldDeserializerLocationExtraItem deserializer = new ExtendedJt808FieldDeserializerLocationExtraItem();
            if (fail) {
                assertSame(failure, assertThrows(IllegalArgumentException.class, () -> read(deserializer, context, "64021234")));
            } else {
                assertEquals(0x1234, ((NestedItem) read(deserializer, context, "64021234").get(0x64)).getValue());
            }
            assertNotNull(content.get());
            assertEquals(0, content.get().refCnt());
        }
    }

    private static RequestFieldAlias.LocationMsgExtraItemMapping actualAnnotation() {
        return JavaBeanMetadataUtils.getBeanMetadata(TypedMessage.class).getFieldMapping().get("items")
                .getAnnotation(RequestFieldAlias.LocationMsgExtraItemMapping.class);
    }

    private static JavaBeanFieldMetadata metadata(RequestFieldAlias.LocationMsgExtraItemMapping annotation) {
        final JavaBeanFieldMetadata metadata = mock(JavaBeanFieldMetadata.class);
        when(metadata.getField()).thenReturn(JavaBeanMetadataUtils.getBeanMetadata(TypedMessage.class).getFieldMapping().get("items").getField());
        when(metadata.getAnnotationCache()).thenAnswer(invocation -> Collections.singletonMap(RequestFieldAlias.LocationMsgExtraItemMapping.class, annotation));
        return metadata;
    }

    private static ValueDescriptor pausedDescriptor(ValueDescriptor actual, CountDownLatch entered, CountDownLatch release) {
        final ValueDescriptor descriptor = mock(ValueDescriptor.class);
        when(descriptor.source()).thenAnswer(invocation -> {
            entered.countDown();
            await(release);
            return actual.source();
        });
        when(descriptor.target()).thenAnswer(invocation -> actual.target());
        return descriptor;
    }

    @SuppressWarnings("unchecked")
    private static Map<Integer, Object> read(ExtendedJt808FieldDeserializerLocationExtraItem deserializer,
                                             Jt808FieldDeserializer.Context context, String hex) {
        final ByteBuf body = Unpooled.buffer().writeBytes(HexStringUtils.hexString2Bytes(hex));
        try {
            return (Map<Integer, Object>) deserializer.deserialize(body, MsgDataType.OBJECT, 0, body.readableBytes(), context);
        } finally {
            try {
                assertEquals(body.writerIndex(), body.readerIndex());
                assertEquals(1, body.refCnt());
            } finally {
                body.release();
            }
        }
    }

    private static void runConcurrently(IntConsumer request) throws Exception {
        final int count = 16;
        final CountDownLatch ready = new CountDownLatch(count);
        final CountDownLatch start = new CountDownLatch(1);
        final ExecutorService executor = Executors.newFixedThreadPool(count);
        final List<Future<?>> futures = new ArrayList<>();
        try {
            for (int i = 0; i < count; i++) {
                final int index = i;
                futures.add(executor.submit(() -> {
                    ready.countDown();
                    await(start);
                    request.accept(index);
                }));
            }
            await(ready);
            start.countDown();
            for (Future<?> future : futures) {
                future.get(10, TimeUnit.SECONDS);
            }
        } finally {
            start.countDown();
            shutdown(executor);
        }
    }

    private static void await(CountDownLatch latch) {
        try {
            assertTrue(latch.await(10, TimeUnit.SECONDS), "synchronization timed out");
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new AssertionError(exception);
        }
    }

    private static void shutdown(ExecutorService executor) throws InterruptedException {
        executor.shutdownNow();
        assertTrue(executor.awaitTermination(10, TimeUnit.SECONDS), "executor did not terminate");
    }
}
