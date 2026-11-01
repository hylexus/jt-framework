package io.github.hylexus.jt.jt808.support.annotation.codec;

import io.github.hylexus.jt.exception.JtIllegalStateException;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestField;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.ResponseField;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.ResponseMsgConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.deserialize.DefaultJt808FieldDeserializerRegistry;
import io.github.hylexus.jt.jt808.support.data.deserialize.extension.AbstractExtendedJt808FieldDeserializer;
import io.github.hylexus.jt.jt808.support.data.serializer.DefaultJt808FieldSerializerRegistry;
import io.github.hylexus.jt.jt808.support.data.serializer.Jt808FieldSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class Jt808ConverterCacheTest {

    @BeforeEach
    void resetConstructors() {
        CountingDeserializer.constructions.set(0);
        CountingDeserializer.usedInstances.clear();
        CountingDeserializer.constructionEntered = null;
        CountingDeserializer.releaseConstruction = null;
        CountingSerializer.constructions.set(0);
        CountingSerializer.usedInstances.clear();
        CountingSerializer.constructionEntered = null;
        CountingSerializer.releaseConstruction = null;
        FailingDeserializer.constructions.set(0);
        FailingSerializer.constructions.set(0);
    }

    @Test
    void customDeserializerTakesPrecedenceAndIsReusedAcrossFieldsAndRequests() {
        final Jt808AnnotationBasedDecoder decoder = decoder();
        decode(decoder);
        decode(decoder);
        assertEquals(1, CountingDeserializer.constructions.get());
        assertSingleInstance(CountingDeserializer.usedInstances);
        final Object firstInstance = CountingDeserializer.usedInstances.peek();
        decode(decoder());
        assertEquals(2, CountingDeserializer.constructions.get());
        assertNotSame(firstInstance, CountingDeserializer.usedInstances.toArray()[4]);
    }

    @Test
    void customSerializerTakesPrecedenceAndIsReusedAcrossFieldsAndRequests() {
        final Jt808AnnotationBasedEncoder encoder = encoder();
        encode(encoder);
        encode(encoder);
        assertEquals(1, CountingSerializer.constructions.get());
        assertSingleInstance(CountingSerializer.usedInstances);
        final Object firstInstance = CountingSerializer.usedInstances.peek();
        encode(encoder());
        assertEquals(2, CountingSerializer.constructions.get());
        assertNotSame(firstInstance, CountingSerializer.usedInstances.toArray()[4]);
    }

    @Test
    void concurrentDeserializerRequestsUseOneCompletelyConstructedInstance() throws Exception {
        final Jt808AnnotationBasedDecoder decoder = decoder();
        final CountDownLatch entered = new CountDownLatch(1);
        final CountDownLatch release = new CountDownLatch(1);
        CountingDeserializer.constructionEntered = entered;
        CountingDeserializer.releaseConstruction = release;
        concurrentlyWhileConstructing(() -> decode(decoder), entered, release, CountingDeserializer.usedInstances);
        assertEquals(1, CountingDeserializer.constructions.get());
        assertEquals(32, CountingDeserializer.usedInstances.size());
        assertSingleInstance(CountingDeserializer.usedInstances);
        decode(decoder);
        assertEquals(1, CountingDeserializer.constructions.get());
        assertSingleInstance(CountingDeserializer.usedInstances);
    }

    @Test
    void concurrentSerializerRequestsUseOneCompletelyConstructedInstance() throws Exception {
        final Jt808AnnotationBasedEncoder encoder = encoder();
        final CountDownLatch entered = new CountDownLatch(1);
        final CountDownLatch release = new CountDownLatch(1);
        CountingSerializer.constructionEntered = entered;
        CountingSerializer.releaseConstruction = release;
        concurrentlyWhileConstructing(() -> encode(encoder), entered, release, CountingSerializer.usedInstances);
        assertEquals(1, CountingSerializer.constructions.get());
        assertEquals(32, CountingSerializer.usedInstances.size());
        assertSingleInstance(CountingSerializer.usedInstances);
        encode(encoder);
        assertEquals(1, CountingSerializer.constructions.get());
        assertSingleInstance(CountingSerializer.usedInstances);
    }

    private static void concurrentlyWhileConstructing(Runnable request, CountDownLatch entered,
                                                       CountDownLatch release, ConcurrentLinkedQueue<Object> used) throws Exception {
        final int count = 16;
        final ExecutorService executor = Executors.newFixedThreadPool(count);
        final CountDownLatch ready = new CountDownLatch(count);
        final CountDownLatch start = new CountDownLatch(1);
        final List<Future<?>> futures = new ArrayList<>();
        try {
            for (int i = 0; i < count; i++) {
                futures.add(executor.submit(() -> {
                    ready.countDown();
                    await(start);
                    request.run();
                }));
            }
            await(ready);
            start.countDown();
            await(entered);
            assertTrue(used.isEmpty(), "requests must not use a converter before its constructor returns");
            release.countDown();
            for (Future<?> future : futures) {
                future.get(10, TimeUnit.SECONDS);
            }
        } finally {
            start.countDown();
            release.countDown();
            executor.shutdownNow();
            assertTrue(executor.awaitTermination(10, TimeUnit.SECONDS));
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

    @Test
    void failedDeserializerConstructionKeepsReflectionExceptionAndRetries() {
        final Jt808AnnotationBasedDecoder decoder = decoder();
        for (int i = 0; i < 2; i++) {
            final ByteBuf body = Unpooled.buffer().writeByte(1);
            try {
                assertConstructionFailure(assertThrows(JtIllegalStateException.class,
                        () -> decoder.decode(FailingRequest.class, new FailingRequest(), body, null)));
                assertEquals(1, body.refCnt());
                assertEquals(0, body.readerIndex());
            } finally {
                body.release();
            }
        }
        assertEquals(2, FailingDeserializer.constructions.get());
    }

    @Test
    void failedSerializerConstructionKeepsReflectionExceptionAndRetries() {
        final Jt808AnnotationBasedEncoder encoder = encoder();
        for (int i = 0; i < 2; i++) {
            assertConstructionFailure(assertThrows(JtIllegalStateException.class,
                    () -> encoder.encodeMsgBody(null, new FailingResponse(), null)));
        }
        assertEquals(2, FailingSerializer.constructions.get());
    }

    private static void assertConstructionFailure(JtIllegalStateException exception) {
        assertEquals(InvocationTargetException.class, exception.getCause().getClass());
        assertEquals(IllegalArgumentException.class, exception.getCause().getCause().getClass());
        assertEquals("constructor failure", exception.getCause().getCause().getMessage());
    }

    private static void assertSingleInstance(ConcurrentLinkedQueue<Object> instances) {
        assertFalse(instances.isEmpty());
        final Object first = instances.peek();
        instances.forEach(instance -> assertSame(first, instance));
    }

    private static Jt808AnnotationBasedDecoder decoder() {
        return new Jt808AnnotationBasedDecoder(new DefaultJt808FieldDeserializerRegistry(true));
    }

    private static Jt808AnnotationBasedEncoder encoder() {
        return new Jt808AnnotationBasedEncoder(new DefaultJt808FieldSerializerRegistry(true));
    }

    private static void decode(Jt808AnnotationBasedDecoder decoder) {
        final ByteBuf body = Unpooled.buffer().writeByte(1).writeByte(2);
        try {
            final CustomRequest request = new CustomRequest();
            decoder.decode(CustomRequest.class, request, body, null);
            assertEquals(8, request.getFirst());
            assertEquals(9, request.getSecond());
            assertEquals(2, body.readerIndex());
        } finally {
            body.release();
        }
    }

    private static void encode(Jt808AnnotationBasedEncoder encoder) {
        final ByteBuf body = encoder.encodeMsgBody(null, new CustomResponse(), null);
        try {
            assertEquals(2, body.readableBytes());
            assertEquals(8, body.readUnsignedByte());
            assertEquals(9, body.readUnsignedByte());
        } finally {
            body.release();
        }
    }

    @Data
    public static class CustomRequest {
        @RequestField(order = 1, dataType = MsgDataType.BYTE, customerFieldDeserializerClass = CountingDeserializer.class)
        private int first;
        @RequestField(order = 2, dataType = MsgDataType.BYTE, customerFieldDeserializerClass = CountingDeserializer.class)
        private int second;
    }

    @Data
    public static class CustomResponse {
        @ResponseField(order = 1, dataType = MsgDataType.BYTE, customerFieldSerializerClass = CountingSerializer.class)
        private int first = 1;
        @ResponseField(order = 2, dataType = MsgDataType.BYTE, customerFieldSerializerClass = CountingSerializer.class)
        private int second = 2;
    }

    public static class CountingDeserializer extends AbstractExtendedJt808FieldDeserializer<Integer> {
        private static final AtomicInteger constructions = new AtomicInteger();
        private static final ConcurrentLinkedQueue<Object> usedInstances = new ConcurrentLinkedQueue<>();
        private static CountDownLatch constructionEntered;
        private static CountDownLatch releaseConstruction;
        private int initialized;

        public CountingDeserializer() {
            constructions.incrementAndGet();
            if (constructionEntered != null) {
                constructionEntered.countDown();
                await(releaseConstruction);
            }
            initialized = 7;
        }

        @Override
        public Integer deserialize(ByteBuf body, MsgDataType type, int start, int length, Context context) {
            usedInstances.add(this);
            assertEquals(7, initialized);
            return body.readUnsignedByte() + initialized;
        }
    }

    public static class CountingSerializer implements Jt808FieldSerializer<Integer> {
        private static final AtomicInteger constructions = new AtomicInteger();
        private static final ConcurrentLinkedQueue<Object> usedInstances = new ConcurrentLinkedQueue<>();
        private static CountDownLatch constructionEntered;
        private static CountDownLatch releaseConstruction;
        private int initialized;

        public CountingSerializer() {
            constructions.incrementAndGet();
            if (constructionEntered != null) {
                constructionEntered.countDown();
                await(releaseConstruction);
            }
            initialized = 7;
        }

        @Override
        public Set<ResponseMsgConvertibleMetadata> getSupportedTypes() {
            return Collections.emptySet();
        }

        @Override
        public void serialize(Integer value, MsgDataType type, ByteBuf body) {
            usedInstances.add(this);
            assertEquals(7, initialized);
            body.writeByte(value + initialized);
        }
    }

    public static class FailingRequest {
        @RequestField(order = 1, dataType = MsgDataType.BYTE, customerFieldDeserializerClass = FailingDeserializer.class)
        private int value;
    }

    public static class FailingResponse {
        @ResponseField(order = 1, dataType = MsgDataType.BYTE, customerFieldSerializerClass = FailingSerializer.class)
        private int value;
    }

    public static class FailingDeserializer extends CountingDeserializer {
        private static final AtomicInteger constructions = new AtomicInteger();

        public FailingDeserializer() {
            constructions.incrementAndGet();
            throw new IllegalArgumentException("constructor failure");
        }
    }

    public static class FailingSerializer extends CountingSerializer {
        private static final AtomicInteger constructions = new AtomicInteger();

        public FailingSerializer() {
            constructions.incrementAndGet();
            throw new IllegalArgumentException("constructor failure");
        }
    }
}
