package io.github.hylexus.jt.jt808.issues.issue90;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JT808ServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private static final Logger log = LoggerFactory.getLogger(JT808ServerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
        try {
            final String hex = ByteBufUtil.hexDump(msg);
            log.info("Received JT808 message (hex): {}", hex);

            // TODO: 业务处理

        } finally {
            // 父类已经释放过了
            // ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("Exception caught", cause);
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("Client connected: {}", ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.info("Client disconnected: {}", ctx.channel().remoteAddress());
    }
}
