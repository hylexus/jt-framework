package io.github.hylexus.jt.data.converter.impl;

import io.github.hylexus.jt.data.MsgDataType;
import io.github.hylexus.jt.data.converter.ConvertibleMetadata;
import io.github.hylexus.jt.data.converter.Jt808MsgDataTypeConverter;
import io.github.hylexus.oaks.utils.BcdOps;

import java.util.Collections;
import java.util.Set;

/**
 * @author hylexus
 * Created At 2019-10-28 9:17 下午
 */
public class ByteArrayToBcdStringDataTypeConverter implements Jt808MsgDataTypeConverter<String> {

    @Override
    public Set<ConvertibleMetadata> getConvertibleTypes() {
        return Collections.singleton(ConvertibleMetadata.forJt808MsgDataType(MsgDataType.BCD, String.class));
    }

    @Override
    public String convert(byte[] bytes, int start, int length) {
        return BcdOps.bytes2BcdStringV2(bytes, start, length);
    }

}
