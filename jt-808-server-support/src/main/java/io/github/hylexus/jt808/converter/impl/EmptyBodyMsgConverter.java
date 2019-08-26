package io.github.hylexus.jt808.converter.impl;

import io.github.hylexus.jt808.converter.MsgConverter;
import io.github.hylexus.jt808.msg.AbstractRequestMsg;

import java.util.Optional;

/**
 * @author hylexus
 * createdAt 2019/2/5
 * @see io.github.hylexus.jt808.msg.BuiltinMsgType#CLIENT_HEART_BEAT
 **/
public class EmptyBodyMsgConverter implements MsgConverter {

    @Override
    public Optional convert2SubMsg(AbstractRequestMsg abstractMsg) {
        return Optional.of(abstractMsg);
    }

}