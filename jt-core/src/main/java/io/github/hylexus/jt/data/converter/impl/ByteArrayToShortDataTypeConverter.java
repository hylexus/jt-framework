package io.github.hylexus.jt.data.converter.impl;

import io.github.hylexus.jt.data.MsgDataType;
import io.github.hylexus.jt.data.converter.ConvertibleMetadata;
import io.github.hylexus.jt.data.converter.Jt808MsgDataTypeConverter;
import io.github.hylexus.oaks.utils.IntBitOps;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author hylexus
 * Created At 2019-10-21 11:31 下午
 */
public class ByteArrayToShortDataTypeConverter implements Jt808MsgDataTypeConverter<Short> {

    private static final Set<ConvertibleMetadata> CONVERTIBLE_METADATA_SET;

    static {
        Set<ConvertibleMetadata> set = new HashSet<>(4);

        set.add(ConvertibleMetadata.forJt808MsgDataType(MsgDataType.WORD, Short.class));
        set.add(ConvertibleMetadata.forJt808MsgDataType(MsgDataType.WORD, short.class));

        set.add(ConvertibleMetadata.forJt808MsgDataType(MsgDataType.BYTE, Short.class));
        set.add(ConvertibleMetadata.forJt808MsgDataType(MsgDataType.BYTE, short.class));

        CONVERTIBLE_METADATA_SET = Collections.unmodifiableSet(set);
    }


    @Override
    public Set<ConvertibleMetadata> getConvertibleTypes() {
        return CONVERTIBLE_METADATA_SET;
    }

    @Override
    public Short convert(byte[] bytes, int start, int length) {
        return (short) IntBitOps.intFromBytes(bytes, start, length);
    }
}
