package io.github.hylexus.jt.jt808.support.data.type;

import io.github.hylexus.jt.jt808.JtProtocolConstant;

import java.nio.charset.Charset;

public interface BytesContainer {

    int length();

    short byteValue();

    byte[] bytesValue();

    int wordValue();

    long dwordValue();

    String bcdValue();

    String stringValue(Charset charset);

    default String stringValue() {
        return this.stringValue(JtProtocolConstant.JT_808_STRING_ENCODING);
    }

}
