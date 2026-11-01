package io.github.hylexus.jt.jt808.support.data.deserialize.extension;

import io.github.hylexus.jt.jt808.spec.Jt808Request;
import io.github.hylexus.jt.jt808.spec.Jt808RequestAware;
import io.github.hylexus.jt.jt808.support.annotation.codec.Jt808AnnotationBasedDecoder;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestFieldAlias;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.extensions.KeyValueMapping;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.extensions.ValueDescriptor;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.data.RequestMsgConvertibleMetadata;
import io.github.hylexus.jt.jt808.support.data.deserialize.DefaultJt808FieldDeserializerRegistry;
import io.github.hylexus.jt.jt808.support.data.deserialize.Jt808FieldDeserializer;
import io.github.hylexus.jt.jt808.support.data.type.byteseq.ByteArrayContainer;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class ExtendedJt808FieldDeserializerLocationExtraItemTest {

    private static final String LOCATION_ITEMS = "01040000005d020201f1310109fe02aabb";

    @Test
    void preservesDeclaredTypesAndAnnotationDefault() {
        final TypedMessage message = decode(decoder(), new TypedMessage(), LOCATION_ITEMS);
        assertLocationItems(message.getItems());
    }

    @Test
    void emptyMappingsUseImplicitAndExplicitDefaults() {
        final Jt808AnnotationBasedDecoder decoder = decoder();
        final EmptyMappingMessage implicit = decode(decoder, new EmptyMappingMessage(), LOCATION_ITEMS);
        assertArrayEquals(new byte[]{0, 0, 0, 93}, ((ByteArrayContainer) implicit.getItems().get(1)).value());
        assertArrayEquals(new byte[]{(byte) 0xaa, (byte) 0xbb}, ((ByteArrayContainer) implicit.getItems().get(0xfe)).value());
        final EmptyRawMappingMessage explicit = decode(decoder, new EmptyRawMappingMessage(), LOCATION_ITEMS);
        assertArrayEquals(new byte[]{0, 0, 0, 93}, (byte[]) explicit.getItems().get(1));
        assertArrayEquals(new byte[]{(byte) 0xaa, (byte) 0xbb}, (byte[]) explicit.getItems().get(0xfe));
    }

    @Test
    void preservesExplicitByteArrayDefaultAndFieldIsolation() {
        final Jt808AnnotationBasedDecoder decoder = decoder();
        final TypedMessage typed = decode(decoder, new TypedMessage(), LOCATION_ITEMS);
        final RawMessage raw = decode(decoder, new RawMessage(), LOCATION_ITEMS);
        assertLocationItems(typed.getItems());
        assertArrayEquals(new byte[]{0, 0, 0, 93}, (byte[]) raw.getItems().get(1));
        assertArrayEquals(new byte[]{(byte) 0xaa, (byte) 0xbb}, (byte[]) raw.getItems().get(0xfe));
    }

    @Test
    void lastDuplicateDeclarationWinsWithoutAdditionalKeyValidation() {
        final DuplicateMappingMessage message = decode(decoder(), new DuplicateMappingMessage(), "310109");
        assertEquals(Integer.class, message.getItems().get(0x31).getClass());
        assertEquals(9, message.getItems().get(0x31));
    }

    @Test
    void preservesWireOrderDuplicateValuesAndMutableIndependentResults() {
        final Jt808AnnotationBasedDecoder decoder = decoder();
        final String hex = "0202000a01040000005d02020014";
        final Map<Integer, Object> first = decode(decoder, new TypedMessage(), hex).getItems();
        final Map<Integer, Object> second = decode(decoder, new TypedMessage(), hex).getItems();
        assertEquals(Arrays.asList(2, 1), new ArrayList<>(first.keySet()));
        assertEquals(20, first.get(2));
        assertEquals(93L, first.get(1));
        first.remove(1);
        first.put(2, 99);
        first.put(3, "caller value");
        assertEquals(20, second.get(2));
        assertEquals(93L, second.get(1));
        assertFalse(second.containsKey(3));
    }

    @Test
    void preservesFixedLengthPriorityAndFieldOrder() {
        final FixedLengthMessage message = decode(decoder(), new FixedLengthMessage(), "013101097f");
        assertEquals(1, message.getFlag());
        assertEquals(9, message.getItems().get(0x31));
        assertEquals(0x7f, message.getTrailer());
    }

    @Test
    void conditionalSkipDoesNotConsumeBytes() {
        final FixedLengthMessage message = decode(decoder(), new FixedLengthMessage(), "007f");
        assertEquals(0, message.getFlag());
        assertNull(message.getItems());
        assertEquals(0x7f, message.getTrailer());
    }

    @Test
    void expressionLengthsRemainSpecificToEachMessageAndPrecedeLengthMethods() {
        final Jt808AnnotationBasedDecoder decoder = decoder();
        final ExpressionLengthMessage first = decode(decoder, new ExpressionLengthMessage(), "033101097f");
        final ExpressionLengthMessage second = decode(decoder, new ExpressionLengthMessage(), "063101083101077e");
        assertEquals(9, first.getItems().get(0x31));
        assertEquals(7, second.getItems().get(0x31));
        assertEquals(0x7f, first.getTrailer());
        assertEquals(0x7e, second.getTrailer());
    }

    @Test
    void lengthMethodsRemainSpecificToEachMessage() {
        final Jt808AnnotationBasedDecoder decoder = decoder();
        final MethodLengthMessage first = decode(decoder, new MethodLengthMessage(), "033101097f");
        final MethodLengthMessage second = decode(decoder, new MethodLengthMessage(), "063101083101077e");
        assertEquals(9, first.getItems().get(0x31));
        assertEquals(7, second.getItems().get(0x31));
        assertEquals(0x7f, first.getTrailer());
        assertEquals(0x7e, second.getTrailer());
    }

    @Test
    void preservesZeroLengthAndEmptyInputBehavior() {
        final Jt808AnnotationBasedDecoder decoder = decoder();
        final ZeroLengthMessage message = decode(decoder, new ZeroLengthMessage(), "310109");
        assertEquals(9, message.getItems().get(0x31));
        final Map<Integer, Object> first = decode(decoder, new TypedMessage(), "").getItems();
        final Map<Integer, Object> second = decode(decoder, new TypedMessage(), "").getItems();
        assertTrue(first.isEmpty());
        assertTrue(second.isEmpty());
        assertNotSame(first, second);
    }

    @Test
    void preservesDifferentMappingsForFieldsInTheSameMessage() {
        final TwoFieldsMessage message = decode(decoder(), new TwoFieldsMessage(), "310109310108");
        assertEquals(Integer.class, message.getFirst().get(0x31).getClass());
        assertEquals(9, message.getFirst().get(0x31));
        assertEquals(Short.class, message.getSecond().get(0x31).getClass());
        assertEquals((short) 8, message.getSecond().get(0x31));
    }

    @Test
    void preservesRegisteredBasicConverterSelection() {
        final DefaultJt808FieldDeserializerRegistry registry = new DefaultJt808FieldDeserializerRegistry(true);
        registry.registerConverter(new Jt808FieldDeserializer<Long>() {
            @Override
            public Set<RequestMsgConvertibleMetadata> getConvertibleTypes() {
                return Collections.singleton(new RequestMsgConvertibleMetadata(MsgDataType.DWORD, Long.class));
            }

            @Override
            public Long deserialize(ByteBuf byteBuf, MsgDataType type, int start, int length) {
                return byteBuf.readUnsignedInt() + 100;
            }
        });
        final TypedMessage message = decode(new Jt808AnnotationBasedDecoder(registry), new TypedMessage(), "01040000005d");
        assertEquals(193L, message.getItems().get(1));
    }

    @Test
    void preservesNestedObjectDecodingAndRequestContext() {
        final Jt808Request request = mock(Jt808Request.class);
        final NestedMessage message = new NestedMessage();
        final ByteBuf body = Unpooled.wrappedBuffer(HexStringUtils.hexString2Bytes("64021234"));
        try {
            decoder().decode(NestedMessage.class, message, body, request);
            final NestedItem item = (NestedItem) message.getItems().get(0x64);
            assertEquals(0x1234, item.getValue());
            assertSame(request, item.getRequest());
            assertEquals(4, body.readerIndex());
            assertEquals(1, body.refCnt());
        } finally {
            body.release();
        }
    }

    static Jt808AnnotationBasedDecoder decoder() {
        return new Jt808AnnotationBasedDecoder(new DefaultJt808FieldDeserializerRegistry(true));
    }

    static <T> T decode(Jt808AnnotationBasedDecoder decoder, T message, String hex) {
        final ByteBuf body = Unpooled.buffer().writeBytes(HexStringUtils.hexString2Bytes(hex));
        try {
            assertSame(message, decoder.decode(message.getClass(), message, body, null));
            assertEquals(body.writerIndex(), body.readerIndex());
            assertEquals(1, body.refCnt());
            return message;
        } finally {
            body.release();
        }
    }

    static void assertLocationItems(Map<Integer, Object> items) {
        assertEquals(Long.class, items.get(1).getClass());
        assertEquals(93L, items.get(1));
        assertEquals(Integer.class, items.get(2).getClass());
        assertEquals(497, items.get(2));
        assertEquals(Integer.class, items.get(0x31).getClass());
        assertEquals(9, items.get(0x31));
        assertTrue(items.get(0xfe) instanceof ByteArrayContainer);
        assertArrayEquals(new byte[]{(byte) 0xaa, (byte) 0xbb}, ((ByteArrayContainer) items.get(0xfe)).value());
    }

    @Data
    public static class TypedMessage {
        @RequestFieldAlias.LocationMsgExtraItemMapping(order = 1, lengthExpression = "#ctx.msgBodyLength()", keyValueMappings = {
                @KeyValueMapping(key = 1, value = @ValueDescriptor(source = MsgDataType.DWORD, target = Long.class)),
                @KeyValueMapping(key = 2, value = @ValueDescriptor(source = MsgDataType.WORD, target = Integer.class)),
                @KeyValueMapping(key = 0x31, value = @ValueDescriptor(source = MsgDataType.BYTE, target = Integer.class))
        })
        private Map<Integer, Object> items;
    }

    @Data
    public static class RawMessage {
        @RequestFieldAlias.LocationMsgExtraItemMapping(order = 1, lengthExpression = "#ctx.msgBodyLength()", keyValueMappings = {
                @KeyValueMapping(key = 1, value = @ValueDescriptor(source = MsgDataType.BYTES, target = byte[].class))
        }, defaultKeyValueMapping = @ValueDescriptor(source = MsgDataType.BYTES, target = byte[].class))
        private Map<Integer, Object> items;
    }

    @Data
    public static class EmptyMappingMessage {
        @RequestFieldAlias.LocationMsgExtraItemMapping(order = 1, lengthExpression = "#ctx.msgBodyLength()", keyValueMappings = {})
        private Map<Integer, Object> items;
    }

    @Data
    public static class EmptyRawMappingMessage {
        @RequestFieldAlias.LocationMsgExtraItemMapping(order = 1, lengthExpression = "#ctx.msgBodyLength()", keyValueMappings = {},
                defaultKeyValueMapping = @ValueDescriptor(source = MsgDataType.BYTES, target = byte[].class))
        private Map<Integer, Object> items;
    }

    @Data
    public static class DuplicateMappingMessage {
        @RequestFieldAlias.LocationMsgExtraItemMapping(order = 1, lengthExpression = "#ctx.msgBodyLength()", keyValueMappings = {
                @KeyValueMapping(key = 0x31, value = @ValueDescriptor(source = MsgDataType.BYTE, target = Short.class)),
                @KeyValueMapping(key = 0x31, value = @ValueDescriptor(source = MsgDataType.BYTE, target = Integer.class)),
                @KeyValueMapping(key = 0x100, value = @ValueDescriptor(source = MsgDataType.BYTES, target = byte[].class))
        })
        private Map<Integer, Object> items;
    }

    @Data
    public static class FixedLengthMessage {
        @RequestFieldAlias.Byte(order = 3)
        private int trailer;

        @RequestFieldAlias.LocationMsgExtraItemMapping(
                order = 2,
                conditionalOn = "#this.flag == 1",
                length = 3,
                lengthExpression = "0",
                lengthMethod = "unusedLength",
                keyValueMappings = {
                        @KeyValueMapping(key = 0x31, value = @ValueDescriptor(source = MsgDataType.BYTE, target = Integer.class))
                }
        )
        private Map<Integer, Object> items;

        @RequestFieldAlias.Byte(order = 1)
        private int flag;

        public int unusedLength() {
            throw new AssertionError("fixed length must take precedence");
        }
    }

    @Data
    public static class ExpressionLengthMessage {
        @RequestFieldAlias.Byte(order = 1)
        private int extraLength;

        @RequestFieldAlias.LocationMsgExtraItemMapping(
                order = 2,
                lengthExpression = "#this.extraLength",
                lengthMethod = "unusedLength",
                keyValueMappings = {
                        @KeyValueMapping(key = 0x31, value = @ValueDescriptor(source = MsgDataType.BYTE, target = Integer.class))
                }
        )
        private Map<Integer, Object> items;

        @RequestFieldAlias.Byte(order = 3)
        private int trailer;

        public int unusedLength() {
            throw new AssertionError("expression must take precedence");
        }
    }

    @Data
    public static class MethodLengthMessage {
        @RequestFieldAlias.Byte(order = 1)
        private int extraLength;

        @RequestFieldAlias.LocationMsgExtraItemMapping(order = 2, lengthMethod = "itemLength", keyValueMappings = {
                @KeyValueMapping(key = 0x31, value = @ValueDescriptor(source = MsgDataType.BYTE, target = Integer.class))
        })
        private Map<Integer, Object> items;

        @RequestFieldAlias.Byte(order = 3)
        private int trailer;

        public int itemLength() {
            return extraLength;
        }
    }

    @Data
    public static class ZeroLengthMessage {
        @RequestFieldAlias.LocationMsgExtraItemMapping(order = 1, lengthExpression = "0", keyValueMappings = {
                @KeyValueMapping(key = 0x31, value = @ValueDescriptor(source = MsgDataType.BYTE, target = Integer.class))
        })
        private Map<Integer, Object> items;
    }

    @Data
    public static class TwoFieldsMessage {
        @RequestFieldAlias.LocationMsgExtraItemMapping(order = 1, length = 3, keyValueMappings = {
                @KeyValueMapping(key = 0x31, value = @ValueDescriptor(source = MsgDataType.BYTE, target = Integer.class))
        })
        private Map<Integer, Object> first;

        @RequestFieldAlias.LocationMsgExtraItemMapping(order = 2, length = 3, keyValueMappings = {
                @KeyValueMapping(key = 0x31, value = @ValueDescriptor(source = MsgDataType.BYTE, target = Short.class))
        })
        private Map<Integer, Object> second;
    }

    @Data
    public static class NestedMessage {
        @RequestFieldAlias.LocationMsgExtraItemMapping(order = 1, lengthExpression = "#ctx.msgBodyLength()", keyValueMappings = {
                @KeyValueMapping(key = 0x64, value = @ValueDescriptor(source = MsgDataType.OBJECT, target = NestedItem.class))
        })
        private Map<Integer, Object> items;
    }

    @Data
    public static class NestedItem implements Jt808RequestAware {
        @RequestFieldAlias.Word(order = 1)
        private int value;
        private Jt808Request request;
    }
}
