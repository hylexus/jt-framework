package io.github.hylexus.jt.jt808.support.codec.impl;

import io.github.hylexus.jt.jt808.support.codec.Jt808RequestRouteExceptionHandler;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultJt808RequestRouteExceptionHandler implements Jt808RequestRouteExceptionHandler {

    @Override
    public void onReceiveUnknownMsg(int msgId, ByteBuf payload) {
        log.error("Received unknown msg, msgId = {}({}). ignore. payload={}",
                msgId, HexStringUtils.int2HexString(msgId, 4), HexStringUtils.byteBufToString(payload));
    }

}
