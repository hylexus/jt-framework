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
public class ByteArrayToIntegerDataTypeConverter implements Jt808MsgDataTypeConverter<Integer> {

    private static final Set<ConvertibleMetadata> CONVERTIBLE_METADATA_SET;

    static {
        Set<ConvertibleMetadata> set = new HashSet<>(6);

        set.add(ConvertibleMetadata.forJt808MsgDataType(MsgDataType.DWORD, Integer.class));
        set.add(ConvertibleMetadata.forJt808MsgDataType(MsgDataType.DWORD, int.class));

        set.add(ConvertibleMetadata.forJt808MsgDataType(MsgDataType.WORD, Integer.class));
        set.add(ConvertibleMetadata.forJt808MsgDataType(MsgDataType.WORD, int.class));

        set.add(ConvertibleMetadata.forJt808MsgDataType(MsgDataType.BYTE, Integer.class));
        set.add(ConvertibleMetadata.forJt808MsgDataType(MsgDataType.BYTE, int.class));

        CONVERTIBLE_METADATA_SET = Collections.unmodifiableSet(set);
    }


    @Override
    public Set<ConvertibleMetadata> getConvertibleTypes() {
        return CONVERTIBLE_METADATA_SET;
    }


    @Override
    public Integer convert(byte[] bytes, int start, int length) {
        return IntBitOps.intFromBytes(bytes, start, length);
    }
}
