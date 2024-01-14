package io.github.hylexus.jt.jt808.support.extension.attachment;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.util.List;

public class DelimiterAndLengthFieldBasedByteToMessageDecoder extends ByteToMessageDecoder {

    static class InternalLengthFieldBasedFrameDecoder extends LengthFieldBasedFrameDecoder {

        public InternalLengthFieldBasedFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
            super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
        }

        // 将父类的 protected final void decode(ChannelHandlerContext, ByteBuf, List<java.lang.Object>) 改成 public 的
        public final void doDecode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
            super.decode(ctx, in, out);
        }

    }

    static class InternalDelimiterBasedFrameDecoder extends DelimiterBasedFrameDecoder {

        public InternalDelimiterBasedFrameDecoder(int maxFrameLength, ByteBuf delimiter) {
            super(maxFrameLength, delimiter);
        }

        // 将父类的 protected final void decode(ChannelHandlerContext, ByteBuf, List<java.lang.Object>) 改成 public 的
        public final void doDecode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
            super.decode(ctx, in, out);
        }

    }

    private final ByteBuf prefix;
    private final InternalLengthFieldBasedFrameDecoder lengthFieldDecoder;
    private final InternalDelimiterBasedFrameDecoder delimiterDecoder;

    public DelimiterAndLengthFieldBasedByteToMessageDecoder(int delimiterBasedFrameMaxFrameLength, int lengthFieldBasedFrameMaxFrameLength) {
        this.prefix = Unpooled.copiedBuffer(new byte[]{0x30, 0x31, 0x63, 0x64});
        final ByteBuf delimiter = Unpooled.copiedBuffer(new byte[]{0x7e});
        this.lengthFieldDecoder = new InternalLengthFieldBasedFrameDecoder(lengthFieldBasedFrameMaxFrameLength, 58, 4, 0, 0);
        this.delimiterDecoder = new InternalDelimiterBasedFrameDecoder(delimiterBasedFrameMaxFrameLength, delimiter);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (this.startWith(in, prefix)) {
            this.lengthFieldDecoder.doDecode(ctx, in, out);
        } else {
            this.delimiterDecoder.doDecode(ctx, in, out);
        }
    }

    boolean startWith(ByteBuf buf, ByteBuf prefix) {
        final int readableBytes = prefix.readableBytes();
        if (readableBytes > buf.readableBytes()) {
            return false;
        }

        for (int i = 0, j = buf.readerIndex(); i < readableBytes; i++, j++) {
            if (prefix.getByte(i) != buf.getByte(j)) {
                return false;
            }
        }
        return true;
    }
}
