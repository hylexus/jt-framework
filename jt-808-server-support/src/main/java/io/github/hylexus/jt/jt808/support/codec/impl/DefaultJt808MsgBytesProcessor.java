
package io.github.hylexus.jt.jt808.support.codec.impl;

import io.github.hylexus.jt.jt808.support.codec.Jt808MsgBytesProcessor;
import io.github.hylexus.jt.jt808.support.exception.Jt808MsgEscapeException;
import io.netty.buffer.ByteBuf;

/**
 * @author hylexus
 */
public class DefaultJt808MsgBytesProcessor implements Jt808MsgBytesProcessor {

    @Override
    public ByteBuf doEscapeForReceive(ByteBuf byteBuf) throws Jt808MsgEscapeException {
        // TODO 转义
        return byteBuf;
    }

    @Override
    public ByteBuf doEscapeForSend(ByteBuf byteBuf) throws Jt808MsgEscapeException {
        // TODO 转义
        return byteBuf;
    }

    @Override
    public byte calculateCheckSum(ByteBuf byteBuf) {
        byte sum = 0;
        while (byteBuf.isReadable()) {
            sum ^= byteBuf.readByte();
        }
        byteBuf.resetReaderIndex();
        return sum;
    }
}

