package io.github.hylexus.jt.jt1078.support.netty;

import io.github.hylexus.jt.common.JtCommonUtils;
import io.github.hylexus.jt.jt1078.spec.Jt1078Request;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionManager;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078MsgDecoder;
import io.github.hylexus.jt.jt1078.support.dispatcher.Jt1078RequestMsgDispatcher;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;

import static io.github.hylexus.jt.jt1078.spec.impl.session.DefaultJt1078SessionCloseReason.CHANNEL_INACTIVE;
import static io.github.hylexus.jt.jt1078.spec.impl.session.DefaultJt1078SessionCloseReason.SERVER_EXCEPTION_OCCURRED;

/**
 * @author hylexus
 */
@Slf4j
@ChannelHandler.Sharable
public class Jt1078DispatcherChannelHandler extends ChannelInboundHandlerAdapter {

    private final Jt1078MsgDecoder msgDecoder;
    private final Jt1078SessionManager sessionManager;
    private final Jt1078RequestMsgDispatcher requestMsgDispatcher;

    public Jt1078DispatcherChannelHandler(Jt1078MsgDecoder msgDecoder, Jt1078SessionManager sessionManager, Jt1078RequestMsgDispatcher requestMsgDispatcher) {
        this.msgDecoder = msgDecoder;
        this.sessionManager = sessionManager;
        this.requestMsgDispatcher = requestMsgDispatcher;
    }

    @Override
    public void channelRead(@Nonnull ChannelHandlerContext ctx, @Nonnull Object msg) {
        if (msg instanceof ByteBuf) {
            final ByteBuf buf = (ByteBuf) msg;
            try {
                if (buf.readableBytes() <= 0) {
                    return;
                }

                Jt1078Request request = null;
                try {
                    if (log.isDebugEnabled()) {
                        log.debug("---> 30316364{}", HexStringUtils.byteBufToHexString(buf));
                    }
                    request = this.msgDecoder.decode(buf);

                    // update session
                    this.sessionManager.persistenceIfNecessary(request.sim(), ctx.channel());
                } catch (Throwable e) {
                    if (request != null) {
                        request.release();
                    }
                    throw e;
                }
                // dispatch
                this.requestMsgDispatcher.doDispatch(request);
                if (log.isDebugEnabled()) {
                    log.debug("{}\n", request);
                }
            } catch (Throwable e) {
                throw new RuntimeException(e);
            } finally {
                JtCommonUtils.release(buf);
            }
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        sessionManager.removeBySessionIdAndClose(sessionManager.generateSessionId(ctx.channel()), SERVER_EXCEPTION_OCCURRED);
        log.error("[exceptionCaught]", cause);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        sessionManager.removeBySessionIdAndClose(sessionManager.generateSessionId(ctx.channel()), CHANNEL_INACTIVE);
    }
}
