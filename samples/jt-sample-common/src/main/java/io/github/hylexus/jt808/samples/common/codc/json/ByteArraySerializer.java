package io.github.hylexus.jt808.samples.common.codc.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class ByteArraySerializer extends StdSerializer<byte[]> {

    ByteArraySerializer() {
        super(byte[].class);
    }

    @Override
    public void serialize(byte[] bytes, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartArray();
        for (byte b : bytes) {
            jsonGenerator.writeNumber(b & 0xff);
        }
        jsonGenerator.writeEndArray();
    }
}