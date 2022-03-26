package io.github.hylexus.jt.jt808.support.netty;

import io.github.hylexus.jt.jt808.spec.session.Jt808SessionManager;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808RequestProcessor;
import io.github.hylexus.jt.jt808.support.utils.JtProtocolUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import static io.github.hylexus.jt.jt808.spec.session.DefaultSessionCloseReason.CHANNEL_INACTIVE;
import static io.github.hylexus.jt.jt808.spec.session.DefaultSessionCloseReason.SERVER_EXCEPTION_OCCURRED;

/**
 * @author hylexus
 * @author lirenhao
 **/
@Slf4j
@ChannelHandler.Sharable
public class Jt808DispatchChannelHandlerAdapter extends ChannelInboundHandlerAdapter {

    private final Jt808SessionManager sessionManager;
    private final Jt808RequestProcessor requestProcessor;

    public Jt808DispatchChannelHandlerAdapter(Jt808RequestProcessor requestProcessor, Jt808SessionManager sessionManager) {
        this.requestProcessor = requestProcessor;
        this.sessionManager = sessionManager;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof ByteBuf) {
            final ByteBuf buf = (ByteBuf) msg;
            try {
                if (buf.readableBytes() <= 0) {
                    return;
                }
                this.requestProcessor.processJt808Request(buf, ctx.channel());
            } finally {
                JtProtocolUtils.release(buf);
            }
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        sessionManager.removeBySessionIdAndClose(sessionManager.generateSessionId(ctx.channel()), SERVER_EXCEPTION_OCCURRED);
        log.error("[exceptionCaught]", cause);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        sessionManager.removeBySessionIdAndClose(sessionManager.generateSessionId(ctx.channel()), CHANNEL_INACTIVE);
    }
}
