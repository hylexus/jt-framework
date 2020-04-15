package io.github.hylexus.jt808.dispatcher.impl;

import io.github.hylexus.jt808.codec.Encoder;
import io.github.hylexus.jt808.converter.ResponseMsgBodyConverter;
import io.github.hylexus.jt808.msg.RespMsgBody;

import java.io.IOException;
import java.util.Optional;

/**
 * @author hylexus
 * Created At 2020-03-11 22:09
 */
public class DefaultCommandSender extends AbstractCommandSender {

    private final ResponseMsgBodyConverter respMsgBodyConverter;
    private final Encoder encoder;

    public DefaultCommandSender(ResponseMsgBodyConverter respMsgBodyConverter, Encoder encoder) {
        this.respMsgBodyConverter = respMsgBodyConverter;
        this.encoder = encoder;
    }

    @Override
    protected byte[] encode(Object object, String terminalId, int flowId) throws IOException {
        final Optional<RespMsgBody> bodyInfo = respMsgBodyConverter.convert(object);
        if (bodyInfo.isPresent()) {
            return this.encoder.encodeRespMsg(bodyInfo.get(), flowId, terminalId);
        }
        return new byte[0];
    }
}
