package io.github.hylexus.jt.jt808.support.codec;

import io.github.hylexus.jt.jt808.spec.Jt808Request;
import io.netty.buffer.ByteBuf;

/**
 * @author hylexus
 */
public interface Jt808MsgDecoder {

    /**
     * 解析请求，返回 {@link Jt808Request} 实例。
     * <p>
     * 如果默认的 {@link io.github.hylexus.jt.jt808.spec.impl.request.DefaultJt808Request DefaultJt808Request} 不满足需求 或
     * {@link  io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808MsgDecoder DefaultJt808MsgDecoder} 不符合要求，
     * 可以提供自己的实现类在这里返回自己的 {@link Jt808Request} 实现
     *
     * @param byteBuf 请求消息(不包含分隔符 {@link io.github.hylexus.jt.jt808.JtProtocolConstant#PACKAGE_DELIMITER 0X7E})
     * @return 解析之后的 {@link Jt808Request} 实例.
     * @see Jt808MsgBytesProcessor#doEscapeForReceive(ByteBuf)
     * @see Jt808MsgBytesProcessor#calculateCheckSum(ByteBuf)
     */
    Jt808Request decode(ByteBuf byteBuf);

}
