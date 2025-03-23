package io.github.hylexus.jt.jt808.spec.utils;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.hylexus.jt.jt808.support.annotation.msg.PrependLengthFieldType;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class DynamicField {
    private MsgDataType type;

    @JsonSerialize(using = DynamicFieldEncoder.DynamicFieldValueJsonSerializer.class)
    private Object value;

    private String encoding;
    private PrependLengthFieldType prependLengthFieldType;

    public static DynamicField ofByte(short value) {
        return new DynamicField(MsgDataType.BYTE, value);
    }

    public static DynamicField ofWord(int value) {
        return new DynamicField(MsgDataType.WORD, value);
    }

    public static DynamicField ofDword(long value) {
        return new DynamicField(MsgDataType.DWORD, value);
    }

    public static DynamicField ofBytes(byte[] value) {
        return ofBytes(value, PrependLengthFieldType.none);
    }

    public static DynamicField ofBytes(byte[] value, PrependLengthFieldType prependLengthFieldType) {
        return new DynamicField(MsgDataType.BYTES, value).setPrependLengthFieldType(prependLengthFieldType);
    }

    public static DynamicField ofBytes(List<Byte> value) {
        return ofBytes(value, PrependLengthFieldType.none);
    }

    public static DynamicField ofBytes(List<Byte> value, PrependLengthFieldType prependLengthFieldType) {
        final byte[] bytes = new byte[value.size()];
        for (int i = 0; i < value.size(); i++) {
            bytes[i] = value.get(i);
        }
        return new DynamicField(MsgDataType.BYTES, bytes).setPrependLengthFieldType(prependLengthFieldType);
    }

    public static DynamicField ofString(String value, String encoding, PrependLengthFieldType prependLengthFieldType) {
        return new DynamicField(MsgDataType.STRING, value, encoding, prependLengthFieldType);
    }

    public static DynamicField ofStringGbk(String value, PrependLengthFieldType prependLengthFieldType) {
        return new DynamicField(MsgDataType.STRING, value, "GBK", prependLengthFieldType);
    }

    public static DynamicField ofBcd(String value, PrependLengthFieldType prependLengthFieldType) {
        return new DynamicField(MsgDataType.BCD, value).setPrependLengthFieldType(prependLengthFieldType);
    }

    public static DynamicField fromMap(Map<String, Object> map) {
        final String typeString = (String) map.get("type");
        final MsgDataType type = MsgDataType.of(typeString)
                .orElseThrow(() -> new IllegalArgumentException("Invalid type: " + typeString));

        final DynamicField field = new DynamicField().setType(type);

        field.setEncoding((String) map.get("encoding"));

        field.setValue(map.get("value"));

        final String prependLengthFieldType = (String) map.get("prependLengthFieldType");
        if (prependLengthFieldType != null) {
            field.setPrependLengthFieldType(PrependLengthFieldType.of(prependLengthFieldType));
        }

        return field;
    }

    public DynamicField(MsgDataType type, Object value) {
        this.type = type;
        this.value = value;
    }

    public DynamicField(MsgDataType type, Object value, String encoding) {
        this.type = type;
        this.value = value;
        this.encoding = encoding;
    }

    public DynamicField(MsgDataType type, Object value, String encoding, PrependLengthFieldType prependLengthFieldType) {
        this.type = type;
        this.value = value;
        this.encoding = encoding;
        this.prependLengthFieldType = prependLengthFieldType;
    }

    public DynamicField() {
    }

    public Number asNumber() {
        return (Number) value;
    }

    public String asString() {
        return (String) value;
    }

    public byte[] asBytes() {
        if (value instanceof byte[]) {
            return (byte[]) value;
        } else if (value instanceof List) {
            final List<?> list = (List<?>) value;
            final byte[] bytes = new byte[list.size()];
            for (int i = 0; i < list.size(); i++) {
                final Object o = list.get(i);
                bytes[i] = ((Number) o).byteValue();
            }
            return bytes;
        } else if (value instanceof String) {
            return ((String) value).getBytes(Charset.forName(this.encoding));
        } else {
            throw new IllegalArgumentException("Invalid value type: " + value.getClass().getName());
        }
    }

    // getters and setters
    public MsgDataType getType() {
        return type;
    }

    public DynamicField setType(MsgDataType type) {
        this.type = type;
        return this;
    }

    public Object getValue() {
        return value;
    }

    public DynamicField setValue(Object value) {
        this.value = value;
        return this;
    }

    public String getEncoding() {
        return encoding;
    }

    public DynamicField setEncoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    public PrependLengthFieldType getPrependLengthFieldType() {
        return prependLengthFieldType;
    }

    public DynamicField setPrependLengthFieldType(PrependLengthFieldType prependLengthFieldType) {
        this.prependLengthFieldType = prependLengthFieldType;
        return this;
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", DynamicField.class.getSimpleName() + "[", "]")
                .add("type=" + type)
                .add("value=" + value)
                .add("encoding='" + encoding + "'")
                .add("prependLengthFieldType=" + prependLengthFieldType)
                .toString();
    }

}
