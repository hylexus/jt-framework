package io.github.hylexus.jt.jt808.support.codec;

import io.github.hylexus.jt.jt808.spec.Jt808Response;
import io.github.hylexus.jt.jt808.spec.session.Jt808FlowIdGenerator;
import io.netty.buffer.ByteBuf;

/**
 * @author hylexus
 */
public interface Jt808MsgEncoder {

    /**
     * 将 {@link Jt808Response} 编码为 {@code 符合808标准} 的字节流
     *
     * @param response 响应消息
     * @return 编码后的字节流
     * @see Jt808MsgBytesProcessor#doEscapeForSend(ByteBuf)
     * @see Jt808MsgBytesProcessor#calculateCheckSum(ByteBuf)
     */
    ByteBuf encode(Jt808Response response, Jt808FlowIdGenerator flowIdGenerator);

}
