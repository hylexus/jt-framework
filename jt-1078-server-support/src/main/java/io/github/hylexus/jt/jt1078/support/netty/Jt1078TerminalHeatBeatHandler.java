package io.github.hylexus.jt.jt1078.support.netty;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import static io.github.hylexus.jt.jt1078.spec.impl.session.DefaultJt1078SessionCloseReason.IDLE_TIMEOUT;


@Slf4j
@ChannelHandler.Sharable
@BuiltinComponent
public class Jt1078TerminalHeatBeatHandler extends ChannelInboundHandlerAdapter {

    protected final Jt1078SessionManager sessionManager;

    public Jt1078TerminalHeatBeatHandler(Jt1078SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * @see <a href="https://github.com/hylexus/jt-framework/issues/66">https://github.com/hylexus/jt-framework/issues/66</a>
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (!(evt instanceof IdleStateEvent)) {
            super.userEventTriggered(ctx, evt);
            return;
        }

        try {
            log.debug("disconnected idle connection, reason: {}", ((IdleStateEvent) evt).state());
            sessionManager.removeBySessionIdAndClose(sessionManager.generateSessionId(ctx.channel()), IDLE_TIMEOUT);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        // 之所以有下面这个再次关闭 channel 的代码, 是因为上面的 sessionManager.removeBySessionIdAndClose() 可能无法真正关闭对应的 channel
        // 比如: 即便是客户端建立了连接，但是客户端没有发送任何消息的情况下, sessionManager 里是不会有对应的session的；
        // 所以通过 session.channel().close() 是关不掉的
        // @see https://github.com/hylexus/jt-framework/issues/66
        try {
            ctx.channel().close();
        } catch (Exception ignored) {
            // ignored
        }

    }
}
