package io.github.hylexus.jt.jt808.support.codec;

import io.netty.buffer.ByteBuf;

/**
 * TODO 这个异常处理逻辑应该加入到全局异常处理器的处理范围内
 *
 * @author hylexus
 * @see <a href="https://github.com/hylexus/jt-framework/issues/78">https://github.com/hylexus/jt-framework/issues/78</a>
 * @see io.github.hylexus.jt.jt808.support.exception.Jt808UnknownMsgException
 */
public interface Jt808RequestRouteExceptionHandler {

    void onReceiveUnknownMsg(int msgId, ByteBuf payload);

}
