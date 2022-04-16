package io.github.hylexus.jt.jt1078.support.netty;

import io.github.hylexus.jt.common.JtCommonUtils;
import io.github.hylexus.jt.jt1078.spec.Jt1078Request;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078MsgDecoder;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;

/**
 * @author hylexus
 */
@Slf4j
@ChannelHandler.Sharable
public class Jt1078DispatcherChannelHandler extends ChannelInboundHandlerAdapter {

    private final Jt1078MsgDecoder msgDecoder;

    public Jt1078DispatcherChannelHandler(Jt1078MsgDecoder msgDecoder) {
        this.msgDecoder = msgDecoder;
    }

    @Override
    public void channelRead(@Nonnull ChannelHandlerContext ctx, @Nonnull Object msg) throws Exception {
        if (msg instanceof ByteBuf) {
            final ByteBuf buf = (ByteBuf) msg;
            try {
                if (buf.readableBytes() <= 0) {
                    return;
                }
                log.info("{}", HexStringUtils.byteBufToHexString(buf));
                final Jt1078Request request = this.msgDecoder.decode(buf);
                // TODO session && pub-sub/type-converter
                log.info("{}", request);
                request.release();
            } finally {
                JtCommonUtils.release(buf);
                System.out.println(buf.refCnt());
            }
        } else {
            ctx.fireChannelRead(msg);
        }
    }
}
