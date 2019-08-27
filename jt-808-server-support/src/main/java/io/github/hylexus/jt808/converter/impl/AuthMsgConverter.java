package io.github.hylexus.jt808.converter.impl;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.utils.ProtocolUtils;
import io.github.hylexus.jt808.converter.MsgConverter;
import io.github.hylexus.jt808.msg.AbstractRequestMsg;
import io.github.hylexus.jt808.msg.req.AuthRequestMsg;

import java.util.Optional;

/**
 * @author hylexus
 * createdAt 2019/2/1
 * @see AuthRequestMsg
 **/
@BuiltinComponent
public class AuthMsgConverter implements MsgConverter<AuthRequestMsg> {

    @Override
    public Optional<AuthRequestMsg> convert2SubMsg(AbstractRequestMsg abstractMsg) {
        AuthRequestMsg ret = new AuthRequestMsg(abstractMsg);
        final byte[] data = abstractMsg.getBodyBytes();
        ret.setAuthCode(ProtocolUtils.bytes2String(data, 0, data.length));
        return Optional.of(ret);
    }

}