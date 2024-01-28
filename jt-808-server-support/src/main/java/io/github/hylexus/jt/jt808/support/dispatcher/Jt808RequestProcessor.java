package io.github.hylexus.jt.jt808.support.dispatcher;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

/**
 * @author hylexus
 */
public interface Jt808RequestProcessor extends InternalJt808RequestProcessor {

    void processJt808Request(ByteBuf byteBuf, Channel channel) throws Exception;

}
