package io.github.hylexus.jt808.converter.impl.resp;

import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.msg.RespMsgBody;
import io.github.hylexus.jt808.msg.resp.VoidRespMsgBody;
import io.github.hylexus.jt808.session.Jt808Session;

import java.util.Optional;

/**
 * 不给客户端回复消息
 *
 * @author hylexus
 * Created At 2020-02-02 2:50 下午
 */
public class VoidRespMsgBodyConverter extends AbstractBuiltinRespBodyConverter {

    @Override
    public boolean supportsMsgBody(Object msgBody) {
        return msgBody == VoidRespMsgBody.NO_DATA_WILL_BE_SENT_TO_CLIENT || msgBody == null;
    }

    @Override
    public Optional<RespMsgBody> convert(Object msgBody, Jt808Session session, RequestMsgMetadata metadata) {
        return Optional.empty();
    }

}
