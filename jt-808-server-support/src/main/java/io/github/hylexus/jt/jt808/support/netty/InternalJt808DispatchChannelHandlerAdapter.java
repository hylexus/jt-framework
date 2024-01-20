package io.github.hylexus.jt.jt808.support.netty;

import io.github.hylexus.jt.annotation.Internal;
import io.github.hylexus.jt.jt808.spec.Jt808RequestLifecycleListener;
import io.github.hylexus.jt.jt808.spec.Jt808RequestLifecycleListenerAware;
import io.github.hylexus.jt.jt808.spec.session.InternalJt808SessionManager;
import io.github.hylexus.jt.jt808.spec.session.Jt808Session;
import io.github.hylexus.jt.jt808.support.dispatcher.InternalJt808RequestProcessor;
import io.github.hylexus.jt.jt808.support.utils.JtProtocolUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;

import static io.github.hylexus.jt.jt808.spec.session.DefaultSessionCloseReason.CHANNEL_INACTIVE;
import static io.github.hylexus.jt.jt808.spec.session.DefaultSessionCloseReason.SERVER_EXCEPTION_OCCURRED;

/**
 * @see Jt808DispatchChannelHandlerAdapter
 * @since 2.1.4
 */
@Slf4j
@Internal
public class InternalJt808DispatchChannelHandlerAdapter
        extends ChannelInboundHandlerAdapter
        implements Jt808RequestLifecycleListenerAware {

    protected final InternalJt808SessionManager sessionManager;
    protected final InternalJt808RequestProcessor requestProcessor;
    protected Jt808RequestLifecycleListener requestLifecycleListener;
    protected final Jt808Session.Role role;

    public InternalJt808DispatchChannelHandlerAdapter(InternalJt808RequestProcessor requestProcessor, InternalJt808SessionManager sessionManager) {
        this.requestProcessor = requestProcessor;
        this.sessionManager = sessionManager;
        this.role = this instanceof Jt808DispatchChannelHandlerAdapter
                ? Jt808Session.Role.INSTRUCTION
                : Jt808Session.Role.ATTACHMENT;
    }

    @Override
    public void channelRead(@Nonnull ChannelHandlerContext ctx, @Nonnull Object msg) throws Exception {
        if (msg instanceof ByteBuf) {
            final ByteBuf buf = (ByteBuf) msg;
            try {
                if (buf.readableBytes() <= 0) {
                    return;
                }
                final boolean continueProcess = this.requestLifecycleListener.beforeDecode(buf, ctx.channel(), this.role);
                if (!continueProcess) {
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
        if (log.isDebugEnabled()) {
            log.warn("channelInactive, address={} ", ctx.channel().remoteAddress());
        }
        sessionManager.removeBySessionIdAndClose(sessionManager.generateSessionId(ctx.channel()), CHANNEL_INACTIVE);
    }

    @Override
    public void setRequestLifecycleListener(Jt808RequestLifecycleListener requestLifecycleListener) {
        this.requestLifecycleListener = requestLifecycleListener;
    }
}
