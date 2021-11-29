package io.github.hylexus.jt.jt808.support.codec;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.request.Jt808Request;

/**
 * @author hylexus
 */
public interface Jt808MsgDecoder {

    Jt808Request decode(Jt808ProtocolVersion version, Jt808ByteBuf byteBuf);

}
