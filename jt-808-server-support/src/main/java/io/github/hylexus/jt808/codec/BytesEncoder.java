package io.github.hylexus.jt808.codec;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.exception.MsgEscapeException;
import io.github.hylexus.jt.utils.ProtocolUtils;

/**
 * 消息转义
 *
 * @author hylexus
 * Created At 2020-01-28 6:59 下午
 */
public interface BytesEncoder {

    /**
     * @param bytes 从客户端接收到的字节数组
     * @param start 开始字节索引
     * @param end   结束字节索引
     * @return 转义后的字节数组
     * @throws MsgEscapeException 转义异常时抛出
     */
    byte[] doEscapeForReceive(byte[] bytes, int start, int end) throws MsgEscapeException;

    /**
     * @param bytes 要发送给客户端的字节数组
     * @param start 开始字节索引
     * @param end   结束字节索引
     * @return 转义后的字节数组
     * @throws MsgEscapeException 转义异常时抛出
     */
    byte[] doEscapeForSend(byte[] bytes, int start, int end) throws MsgEscapeException;

    /**
     * @param bs    要计算校验码的字节数组
     * @param start 开始位置
     * @param end   结束位置
     * @return 校验码
     */
    default byte calculateCheckSum(byte[] bs, int start, int end) {
        return ProtocolUtils.calculateCheckSum4Jt808(bs, start, end);
    }

    @BuiltinComponent
    class DefaultBytesEncoder implements BytesEncoder {

        @Override
        public byte[] doEscapeForReceive(byte[] bytes, int start, int end) throws MsgEscapeException {
            try {
                return ProtocolUtils.doEscape4ReceiveJt808Msg(bytes, start, end);
            } catch (Exception e) {
                throw new MsgEscapeException(e);
            }
        }

        @Override
        public byte[] doEscapeForSend(byte[] bytes, int start, int end) throws MsgEscapeException {
            try {
                return ProtocolUtils.doEscape4SendJt808Msg(bytes, start, end);
            } catch (Exception e) {
                throw new MsgEscapeException(e);
            }
        }
    }
}
