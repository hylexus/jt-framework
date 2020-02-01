package io.github.hylexus.jt808.samples.annotation.handler;

import io.github.hylexus.jt.annotation.msg.handler.Jt808RequestMsgHandler;
import io.github.hylexus.jt.annotation.msg.handler.Jt808RequestMsgMapping;
import io.github.hylexus.jt808.msg.RequestMsgHeader;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.msg.RespMsgBody;
import io.github.hylexus.jt808.msg.resp.CommonReplyMsgBody;
import io.github.hylexus.jt808.samples.annotation.entity.req.LocationUploadMsgBody;
import io.github.hylexus.jt808.session.Session;

import java.util.List;
import java.util.Map;

/**
 * @author hylexus
 * Created At 2020-02-01 2:54 下午
 */
@Jt808RequestMsgHandler
public class CommonHandler {

    @Jt808RequestMsgMapping(msgType = 0x0200)
    public RespMsgBody processLocationMsg(
            Session session,
            RequestMsgMetadata metadata,
            RequestMsgHeader header,
            LocationUploadMsgBody body,
            List<String> temp,
            Map<String, Object> map) {
        return new CommonReplyMsgBody();
    }

}
