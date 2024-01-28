package io.github.hylexus.jt.jt808.support.extension.attachment;

import io.github.hylexus.jt.jt808.support.netty.InternalJt808DispatchChannelHandlerAdapter;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hylexus
 * @since 2.1.4
 **/
@Slf4j
@ChannelHandler.Sharable
public class AttachmentJt808DispatchChannelHandlerAdapter extends InternalJt808DispatchChannelHandlerAdapter {

    public AttachmentJt808DispatchChannelHandlerAdapter(AttachmentJt808RequestProcessor requestProcessor) {
        super(requestProcessor, null);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("[exceptionCaught]", cause);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("channelInactive, address={} ", ctx.channel().remoteAddress());
        }
    }
}
