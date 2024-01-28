package io.github.hylexus.jt.jt808.support.dispatcher;

import io.github.hylexus.jt.annotation.Internal;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

/**
 * @author hylexus
 * @since 2.1.4
 */
@Internal
public interface InternalJt808RequestProcessor {

    void processJt808Request(ByteBuf byteBuf, Channel channel) throws Exception;

}
