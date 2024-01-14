package io.github.hylexus.jt.jt808.support.extension.attachment;

import io.github.hylexus.jt.jt808.spec.Jt808RequestLifecycleListener;
import io.github.hylexus.jt.jt808.spec.Jt808RequestLifecycleListenerAware;
import io.github.hylexus.jt.jt808.support.utils.JtProtocolUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;

import static io.github.hylexus.jt.jt808.spec.session.DefaultSessionCloseReason.CHANNEL_INACTIVE;
import static io.github.hylexus.jt.jt808.spec.session.DefaultSessionCloseReason.SERVER_EXCEPTION_OCCURRED;

/**
 * @author hylexus
 **/
@Slf4j
@ChannelHandler.Sharable
public class AttachmentJt808DispatchChannelHandlerAdapter extends ChannelInboundHandlerAdapter implements Jt808RequestLifecycleListenerAware {

    private final AttachmentJt808SessionManager sessionManager;
    private final AttachmentJt808RequestProcessor requestProcessor;
    private Jt808RequestLifecycleListener requestLifecycleListener;

    public AttachmentJt808DispatchChannelHandlerAdapter(AttachmentJt808RequestProcessor requestProcessor, AttachmentJt808SessionManager sessionManager) {
        this.requestProcessor = requestProcessor;
        this.sessionManager = sessionManager;
    }

    @Override
    public void channelRead(@Nonnull ChannelHandlerContext ctx, @Nonnull Object msg) throws Exception {
        if (msg instanceof ByteBuf) {
            final ByteBuf buf = (ByteBuf) msg;
            try {
                if (buf.readableBytes() <= 0) {
                    return;
                }
                final boolean continueProcess = this.requestLifecycleListener.beforeDecode(buf, ctx.channel());
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
