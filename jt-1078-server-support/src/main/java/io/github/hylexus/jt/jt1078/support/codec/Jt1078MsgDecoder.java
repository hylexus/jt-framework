package io.github.hylexus.jt.jt1078.support.codec;

import io.github.hylexus.jt.jt1078.spec.Jt1078Request;
import io.netty.buffer.ByteBuf;

/**
 * @author hylexus
 */
public interface Jt1078MsgDecoder {

    Jt1078Request decode(ByteBuf byteBuf);

}
