package io.github.hylexus.jt.utils;

import io.github.hylexus.jt.config.JtProtocolConstant;
import io.github.hylexus.oaks.utils.BcdOps;
import io.github.hylexus.oaks.utils.IntBitOps;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.function.Function;

/**
 * Created At 2020/12/5 2:22 下午
 *
 * @author hylexus
 */
public class MessageBuilder {

    protected final ByteBuf buf = Unpooled.buffer();
    protected final Function<byte[], byte[]> escapeFunction;
    protected final Function<byte[], Byte> checksumCalculator;

    private MessageBuilder() {
        this(
                bytes -> ProtocolUtils.doEscape4SendJt808Msg(bytes, 0, bytes.length - 1),
                bytes -> ProtocolUtils.calculateCheckSum4Jt808(bytes, 0, bytes.length)
        );
    }

    private MessageBuilder(Function<byte[], byte[]> escapeFunction, Function<byte[], Byte> checksumCalculator) {
        this.escapeFunction = escapeFunction;
        this.checksumCalculator = checksumCalculator;
    }

    public static MessageBuilder newBuilder() {
        return new MessageBuilder();
    }

    public MessageBuilder append(byte[] bytes) {
        buf.writeBytes(bytes);
        return this;
    }

    public MessageBuilder appendByte(int b) {
        return this.appendByte((byte) b);
    }

    public MessageBuilder appendByte(byte b) {
        buf.writeByte(b);
        return this;
    }

    public MessageBuilder appendBytes(byte[] bytes) {
        return this.append(bytes);
    }

    public MessageBuilder appendWord(int data) {
        append(IntBitOps.intTo2Bytes(data));
        return this;
    }

    public MessageBuilder appendDword(int data) {
        append(IntBitOps.intTo4Bytes(data));
        return this;
    }

    public MessageBuilder appendBcd(String bcd) {
        append(BcdOps.bcdString2bytes(bcd));
        return this;
    }

    public MessageBuilder appendString(String string) {
        append(string.getBytes(JtProtocolConstant.JT_808_STRING_ENCODING));
        return this;
    }

    // 010000300131660488880D4A000B000031323335394D5239383034000000000000000000000000000061626F0000000001313331363034363636363653
    public byte[] build() {
        return build(true);
    }

    public byte[] build(boolean escape) {
        byte[] bytes = new byte[buf.readableBytes()];
        buf.getBytes(0, bytes);
        if (escape) {
            return escapeFunction.apply(bytes);
        }
        return bytes;
    }

    public Jt808MessageBuilder toJt808MessageBuilder() {
        return Jt808MessageBuilder.newBuilder(this, this.checksumCalculator, this.escapeFunction);
    }
}
