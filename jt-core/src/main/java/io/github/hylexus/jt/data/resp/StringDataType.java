package io.github.hylexus.jt.data.resp;

import io.github.hylexus.jt.config.JtProtocolConstant;
import lombok.Value;

@Value
public class StringDataType implements DataType<String> {

    private final String value;

    private StringDataType(String value) {
        this.value = value;
    }

    public static StringDataType of(String value) {
        return new StringDataType(value);
    }

    @Override
    public byte[] getAsBytes() {
        return value.getBytes(JtProtocolConstant.JT_808_STRING_ENCODING);
    }
}