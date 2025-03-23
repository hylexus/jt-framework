package io.github.hylexus.jt.jt808.spec.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.hylexus.jt.jt808.support.annotation.msg.PrependLengthFieldType;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.utils.JtProtocolUtils;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

public class DynamicFieldEncoder {

    public void encodeFieldsWithListMap(List<Map<String, Object>> fieldList, ByteBuf buffer) {
        for (final Map<String, Object> map : fieldList) {
            final DynamicField field = DynamicField.fromMap(map);
            if (field.getValue() == null) {
                continue;
            }
            this.encodeField(field, buffer);
        }
    }

    public void encodeFields(List<DynamicField> fieldList, ByteBuf buffer) {
        for (final DynamicField field : fieldList) {
            if (field.getValue() == null) {
                continue;
            }
            this.encodeField(field, buffer);
        }
    }

    protected void encodeField(DynamicField field, ByteBuf buffer) {
        final PrependLengthFieldType prependLengthFieldType = field.getPrependLengthFieldType();
        if (prependLengthFieldType == null || prependLengthFieldType == PrependLengthFieldType.none) {
            this.doEncodeFieldValue(field, buffer);
        } else {
            final int lengthFieldWriterIndex = buffer.writerIndex();
            // 前置长度的占位符
            prependLengthFieldType.writeTo(buffer, 0);
            final int beforeEncode = buffer.writerIndex();

            this.doEncodeFieldValue(field, buffer);

            final int afterEncode = buffer.writerIndex();
            final int byteCounts = afterEncode - beforeEncode;

            buffer.writerIndex(lengthFieldWriterIndex);
            // 写入长度字段
            prependLengthFieldType.writeTo(buffer, byteCounts);
            buffer.writerIndex(afterEncode);
        }
    }

    protected void doEncodeFieldValue(DynamicField field, ByteBuf buffer) {
        final MsgDataType type = field.getType();
        switch (type) {
            case BYTE: {
                buffer.writeByte(field.asNumber().intValue());
                break;
            }
            case WORD: {
                buffer.writeShort(field.asNumber().intValue());
                break;
            }
            case DWORD: {
                buffer.writeInt(field.asNumber().intValue());
                break;
            }
            case STRING:
                buffer.writeCharSequence(field.asString(), Charset.forName(field.getEncoding()));
                break;
            case BCD: {
                JtProtocolUtils.writeBcd(buffer, field.asString());
                break;
            }
            case BYTES:
                buffer.writeBytes(field.asBytes());
                break;
            case LIST: {
                final List<?> value = (List<?>) field.getValue();
                for (final Object item : value) {
                    if (item instanceof DynamicField) {
                        this.encodeField((DynamicField) item, buffer);
                    } else if (item instanceof Map) {
                        @SuppressWarnings("unchecked") final DynamicField parsedField = DynamicField.fromMap((Map<String, Object>) item);
                        this.encodeField(parsedField, buffer);
                    } else {
                        throw new IllegalArgumentException("Unsupported type: " + type);
                    }
                }
                break;
            }
            default: {
                throw new IllegalArgumentException("Unsupported type: " + type);
            }
        }
    }

    public static class DynamicFieldValueJsonSerializer extends JsonSerializer<Object> {

        @Override
        public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
            // jackson 会把 byte[] 序列化成 base64 字符串，这里手动处理下
            if (value instanceof byte[]) {
                writeJsonArray((byte[]) value, jsonGenerator);
            } else {
                provider.defaultSerializeValue(value, jsonGenerator);
            }
        }

        private static void writeJsonArray(byte[] array, JsonGenerator jsonGenerator) throws IOException {
            jsonGenerator.writeStartArray();
            for (byte b : array) {
                jsonGenerator.writeNumber(b);
            }
            jsonGenerator.writeEndArray();
        }

    }
}
