package io.github.hylexus.jt.builder;

import io.github.hylexus.jt.config.JtProtocolConstant;
import io.github.hylexus.oaks.utils.BcdOps;
import io.github.hylexus.oaks.utils.IntBitOps;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Created At 2020/12/5 2:22 下午
 *
 * @author hylexus
 */
public abstract class MsgBuilder {

    protected final ByteBuf buf = Unpooled.buffer();

    public MsgBuilder append(byte[] bytes) {
        buf.writeBytes(bytes);
        return this;
    }

    public MsgBuilder appendByte(int b) {
        return this.appendByte((byte) b);
    }

    public MsgBuilder appendByte(byte b) {
        buf.writeByte(b);
        return this;
    }

    public MsgBuilder appendBytes(byte[] bytes) {
        return this.append(bytes);
    }

    public MsgBuilder appendWord(int data) {
        append(IntBitOps.intTo2Bytes(data));
        return this;
    }

    public MsgBuilder appendDword(int data) {
        append(IntBitOps.intTo4Bytes(data));
        return this;
    }

    public MsgBuilder appendBcd(String bcd) {
        append(BcdOps.bcdString2bytes(bcd));
        return this;
    }

    public MsgBuilder appendString(String string) {
        append(string.getBytes(JtProtocolConstant.JT_808_STRING_ENCODING));
        return this;
    }

    public byte[] build() {
        byte[] bytes = new byte[buf.readableBytes()];
        buf.getBytes(0, bytes);
        return bytes;
    }

}
