package io.github.hylexus.jt.data.converter.impl;

import io.github.hylexus.jt.data.MsgDataType;
import io.github.hylexus.jt.data.converter.ConvertibleMetadata;
import io.github.hylexus.jt.data.converter.Jt808MsgDataTypeConverter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author hylexus
 * Created At 2019-10-28 10:39 下午
 */
public class ByteArrayToByteDataTypeConverter implements Jt808MsgDataTypeConverter<Byte> {

    private static final Set<ConvertibleMetadata> CONVERTIBLE_METADATA_SET;

    static {
        Set<ConvertibleMetadata> tmp = new HashSet<>();
        tmp.add(ConvertibleMetadata.forJt808MsgDataType(MsgDataType.BYTE, Byte.class));
        tmp.add(ConvertibleMetadata.forJt808MsgDataType(MsgDataType.BYTE, byte.class));
        CONVERTIBLE_METADATA_SET = Collections.unmodifiableSet(tmp);
    }

    @Override
    public Byte convert(byte[] bytes, int start, int length) {
        return bytes[start];
    }

    @Override
    public Set<ConvertibleMetadata> getConvertibleTypes() {
        return CONVERTIBLE_METADATA_SET;
    }
}
