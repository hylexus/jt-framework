package io.github.hylexus.jt.jt808.support.extension.attachment;

import io.github.hylexus.jt.jt808.support.netty.InternalJt808DispatchChannelHandlerAdapter;
import io.netty.channel.ChannelHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hylexus
 * @since 2.1.4
 **/
@Slf4j
@ChannelHandler.Sharable
public class AttachmentJt808DispatchChannelHandlerAdapter extends InternalJt808DispatchChannelHandlerAdapter {

    public AttachmentJt808DispatchChannelHandlerAdapter(AttachmentJt808RequestProcessor requestProcessor, AttachmentJt808SessionManager sessionManager) {
        super(requestProcessor, sessionManager);
    }

}
