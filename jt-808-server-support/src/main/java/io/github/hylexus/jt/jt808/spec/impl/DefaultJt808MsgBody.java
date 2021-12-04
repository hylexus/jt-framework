package io.github.hylexus.jt.jt808.spec.impl;

import io.github.hylexus.jt.jt808.spec.Jt808MsgBody;
import io.github.hylexus.jt.jt808.support.codec.Jt808ByteBuf;
import io.netty.buffer.ByteBuf;

/**
 * @author hylexus
 */
public class DefaultJt808MsgBody implements Jt808MsgBody {
    private final Jt808ByteBuf byteBuf;

    public DefaultJt808MsgBody(Jt808ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
    }

    @Override
    public Jt808ByteBuf requestBody() {
        return byteBuf;
    }

    public static class DefaultJt808MsgBodySpecBuilder {
        private final Jt808ByteBuf byteBuf;

        public DefaultJt808MsgBodySpecBuilder(Jt808ByteBuf byteBuf) {
            this.byteBuf = byteBuf;
        }

        public DefaultJt808MsgBodySpecBuilder(ByteBuf byteBuf) {
            this(new Jt808ByteBuf(byteBuf));
        }

        public static DefaultJt808MsgBodySpecBuilder newBuilder(ByteBuf byteBuf) {
            return new DefaultJt808MsgBodySpecBuilder(byteBuf);
        }

        public static DefaultJt808MsgBodySpecBuilder newBuilder(Jt808ByteBuf byteBuf) {
            return new DefaultJt808MsgBodySpecBuilder(byteBuf);
        }

        public DefaultJt808MsgBodySpecBuilder writeWord(int value) {
            this.byteBuf.writeWord(value);
            return this;
        }

        public DefaultJt808MsgBodySpecBuilder writeDword(int value) {
            this.byteBuf.writeDword(value);
            return this;
        }

        public DefaultJt808MsgBodySpecBuilder writeString(String value) {
            this.byteBuf.writeString(value);
            return this;
        }

        public DefaultJt808MsgBodySpecBuilder writeBcd(String value) {
            this.byteBuf.writeBcd(value);
            return this;
        }

        public DefaultJt808MsgBodySpecBuilder writeBytes(byte[] bytes) {
            this.byteBuf.writeBytes(bytes);
            return this;
        }

        public DefaultJt808MsgBodySpecBuilder writeByte(byte b) {
            this.byteBuf.writeByte(b);
            return this;
        }

        public DefaultJt808MsgBody build() {
            return new DefaultJt808MsgBody(this.byteBuf);
        }

    }
}
