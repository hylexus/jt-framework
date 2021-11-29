package io.github.hylexus.jt.jt808.support.codec;

import io.github.hylexus.jt.jt808.support.exception.Jt808MsgEscapeException;
import io.netty.buffer.ByteBuf;

/**
 * @author hylexus
 */
public interface Jt808MsgBytesProcessor {

    ByteBuf doEscapeForReceive(ByteBuf byteBuf) throws Jt808MsgEscapeException;

    ByteBuf doEscapeForSend(ByteBuf byteBuf) throws Jt808MsgEscapeException;

    byte calculateCheckSum(ByteBuf byteBuf);
}
