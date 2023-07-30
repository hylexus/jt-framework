package io.github.hylexus.jt808.samples.common.codc.json;

public class ByteArrayModule extends com.fasterxml.jackson.databind.module.SimpleModule {
    public ByteArrayModule() {
        addSerializer(byte[].class, new ByteArraySerializer());
    }
}
