package io.github.hylexus.jt.jt808.support.extension.attachment;

import io.github.hylexus.jt.jt808.support.dispatcher.InternalJt808RequestProcessor;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

public interface AttachmentJt808RequestProcessor extends InternalJt808RequestProcessor {
    void processJt808Request(ByteBuf byteBuf, Channel channel) throws Exception;
}
