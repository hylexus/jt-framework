package io.github.hylexus.jt.jt808.support.codec;

import io.github.hylexus.jt.jt808.support.exception.Jt808MsgEscapeException;
import io.netty.buffer.ByteBuf;

/**
 * @author hylexus
 */
public interface Jt808MsgBytesProcessor {

    /**
     * 转义请求消息
     *
     * @param byteBuf 请求消息, 不包含分隔符 {@link io.github.hylexus.jt.jt808.JtProtocolConstant#PACKAGE_DELIMITER 0X7E}
     * @return 转义之后的消息
     */
    ByteBuf doEscapeForReceive(ByteBuf byteBuf) throws Jt808MsgEscapeException;

    /**
     * 转义响应消息
     *
     * @param byteBuf 响应给客户端的消息, 不包含分隔符 {@link io.github.hylexus.jt.jt808.JtProtocolConstant#PACKAGE_DELIMITER 0X7E}
     * @return 转义之后的消息
     */
    ByteBuf doEscapeForSend(ByteBuf byteBuf) throws Jt808MsgEscapeException;

    /**
     * 就是校验码
     *
     * @param byteBuf 请求消息/响应消息
     * @return 检验码
     */
    byte calculateCheckSum(ByteBuf byteBuf);
}
