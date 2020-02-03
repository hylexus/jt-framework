package io.github.hylexus.jt808.converter.impl.resp;

import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.msg.RespMsgBody;
import io.github.hylexus.jt808.session.Session;

import java.util.Optional;

/**
 * @author hylexus
 * Created At 2020-02-02 3:21 下午
 */
public class NoOpsRespMsgBodyConverter extends AbstractBuiltinRespBodyConverter {

    @Override
    public boolean supportsMsgBody(Object msgBody) {
        return msgBody instanceof RespMsgBody;
    }

    @Override
    public Optional<RespMsgBody> convert(Object msgBody, Session session, RequestMsgMetadata metadata) {
        return Optional.of((RespMsgBody) msgBody);
    }

}
