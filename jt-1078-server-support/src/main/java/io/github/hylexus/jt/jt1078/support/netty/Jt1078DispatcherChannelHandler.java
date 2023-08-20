package io.github.hylexus.jt.jt1078.support.netty;

import io.github.hylexus.jt.common.JtCommonUtils;
import io.github.hylexus.jt.jt1078.spec.Jt1078Session;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionManager;
import io.github.hylexus.jt.jt1078.support.dispatcher.Jt1078RequestPreprocessor;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;

import static io.github.hylexus.jt.jt1078.spec.Jt1078SessionManager.ATTR_KEY_SESSION;
import static io.github.hylexus.jt.jt1078.spec.impl.session.DefaultJt1078SessionCloseReason.CHANNEL_INACTIVE;

/**
 * @author hylexus
 */
@Slf4j
@ChannelHandler.Sharable
public class Jt1078DispatcherChannelHandler extends ChannelInboundHandlerAdapter {


    private final Jt1078SessionManager sessionManager;

    private final Jt1078RequestPreprocessor preprocessor;

    public Jt1078DispatcherChannelHandler(Jt1078SessionManager sessionManager, Jt1078RequestPreprocessor preprocessor) {
        this.sessionManager = sessionManager;
        this.preprocessor = preprocessor;
    }

    @Override
    public void channelRead(@Nonnull ChannelHandlerContext ctx, @Nonnull Object msg) {
        if (msg instanceof ByteBuf) {
            final ByteBuf buf = (ByteBuf) msg;
            try {
                this.preprocessor.preprocess(ctx.channel(), (ByteBuf) msg);
            } finally {
                JtCommonUtils.release(buf);
            }
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // sessionManager.removeBySessionIdAndClose(sessionManager.generateSessionId(ctx.channel()), SERVER_EXCEPTION_OCCURRED);
        log.error("[exceptionCaught]", cause);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        final Jt1078Session session = ctx.channel().attr(ATTR_KEY_SESSION).get();
        if (session != null) {
            sessionManager.removeBySessionIdAndThenClose(session.sessionId(), CHANNEL_INACTIVE);
        }

        try {
            ctx.channel().close().sync();
        } catch (InterruptedException ignored) {
            // ignored
        }
    }

}
