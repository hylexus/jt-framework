package io.github.hylexus.jt808.converter.impl.resp;

import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.msg.RespMsgBody;
import io.github.hylexus.jt808.msg.resp.VoidRespMsgBody;
import io.github.hylexus.jt808.session.Jt808Session;

import java.util.Optional;

/**
 * @author hylexus
 * Created At 2020-02-02 3:21 下午
 */
public class NoOpsRespMsgBodyConverter extends AbstractBuiltinRespBodyConverter {

    @Override
    public boolean supportsMsgBody(Object msgBody) {
        return msgBody != VoidRespMsgBody.NO_DATA_WILL_BE_SENT_TO_CLIENT && msgBody instanceof RespMsgBody;
    }

    @Override
    public Optional<RespMsgBody> convert(Object msgBody, Jt808Session session, RequestMsgMetadata metadata) {
        // mosBody 本身就是 RespMsgBody 类型
        return Optional.of((RespMsgBody) msgBody);
    }

}
