package io.github.hylexus.jt.data.converter.impl;

import io.github.hylexus.jt.data.MsgDataType;
import io.github.hylexus.jt.data.converter.ConvertibleMetadata;
import io.github.hylexus.jt.data.converter.Jt808MsgDataTypeConverter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author hylexus
 * Created At 2021-02-14 18:31 下午
 */
public class ByteArrayToLongDataTypeConverter implements Jt808MsgDataTypeConverter<Long> {

    private static final long MASK = 0xFF;
    private static final Set<ConvertibleMetadata> CONVERTIBLE_METADATA_SET;

    static {
        Set<ConvertibleMetadata> set = new HashSet<>(6);

        set.add(ConvertibleMetadata.forJt808MsgDataType(MsgDataType.DWORD, Long.class));
        set.add(ConvertibleMetadata.forJt808MsgDataType(MsgDataType.DWORD, long.class));

        set.add(ConvertibleMetadata.forJt808MsgDataType(MsgDataType.WORD, Long.class));
        set.add(ConvertibleMetadata.forJt808MsgDataType(MsgDataType.WORD, long.class));

        set.add(ConvertibleMetadata.forJt808MsgDataType(MsgDataType.BYTE, Long.class));
        set.add(ConvertibleMetadata.forJt808MsgDataType(MsgDataType.BYTE, long.class));

        CONVERTIBLE_METADATA_SET = Collections.unmodifiableSet(set);
    }


    @Override
    public Set<ConvertibleMetadata> getConvertibleTypes() {
        return CONVERTIBLE_METADATA_SET;
    }


    @Override
    public Long convert(byte[] bytes, int start, int length) {
        return getLong(bytes, start, length);
    }

    public static long getLong(byte[] bytes, final int startIndex, int byteCount) {
        long value = 0;
        for (int i = 0; i < byteCount; i++) {
            // (8 - 1 - i) * 8
            // (8 - 1 - i) << 3
            value |= ((bytes[startIndex + i] & MASK) << ((byteCount - 1 - i) << 3));
        }
        return value;
    }

}
