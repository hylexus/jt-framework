package io.github.hylexus.jt.jt808.support.codec;

import io.github.hylexus.jt.jt808.response.Jt808Response;
import io.netty.buffer.ByteBuf;

/**
 * @author hylexus
 */
public interface Jt808MsgEncoder {
    ByteBuf encode(Jt808Response response);
}
