package io.github.hylexus.jt.jt808.response.impl;

import io.github.hylexus.jt.jt808.spec.Jt808MsgBodySpec;
import io.github.hylexus.jt.jt808.spec.Jt808MsgHeaderSpec;
import io.github.hylexus.jt.jt808.spec.impl.DefaultJt808MsgHeaderSpec;

import java.util.function.Consumer;

/**
 * @author hylexus
 */
public class DefaultJt808ResponseBuilder {
    private Jt808MsgHeaderSpec header;
    private Jt808MsgBodySpec body;
    private byte checkSum;

    public static DefaultJt808ResponseBuilder newBuilder() {
        return new DefaultJt808ResponseBuilder();
    }

    public DefaultJt808ResponseBuilder withHeader(Jt808MsgHeaderSpec header) {
        this.header = header;
        return this;
    }

    public DefaultJt808ResponseBuilder withHeader(Consumer<DefaultJt808MsgHeaderSpec.DefaultJt808MsgHeaderSpecBuilder> builder) {
        final DefaultJt808MsgHeaderSpec.DefaultJt808MsgHeaderSpecBuilder specBuilder = new DefaultJt808MsgHeaderSpec.DefaultJt808MsgHeaderSpecBuilder();
        builder.accept(specBuilder);
        this.header = specBuilder.build();
        return this;
    }

    public DefaultJt808ResponseBuilder withBody(Jt808MsgBodySpec body) {
        this.body = body;
        return this;
    }

    public DefaultJt808ResponseBuilder withCheckSum(byte checkSum) {
        this.checkSum = checkSum;
        return this;
    }
//
//    public DefaultJt808Response build() {
//        // TODO checkSum
//        return new DefaultJt808Response(header, body, checkSum);
//    }
}
