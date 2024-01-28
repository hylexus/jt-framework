package io.github.hylexus.jt.jt808.support.extension.attachment;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hylexus
 */
@Slf4j
@ChannelHandler.Sharable
@BuiltinComponent
public class AttachmentJt808TerminalHeatBeatHandler extends ChannelInboundHandlerAdapter {

    public AttachmentJt808TerminalHeatBeatHandler() {
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (!(evt instanceof IdleStateEvent)) {
            super.userEventTriggered(ctx, evt);
            return;
        }

        try {
            ctx.channel().close();
        } catch (Exception ignored) {
            // ignored
        }

    }
}
