package io.github.hylexus.jt808.samples.annotation.handler;

import io.github.hylexus.jt.annotation.msg.handler.Jt808RequestMsgHandler;
import io.github.hylexus.jt.annotation.msg.handler.Jt808RequestMsgMapping;
import io.github.hylexus.jt.data.msg.BuiltinJt808MsgType;
import io.github.hylexus.jt808.msg.RequestMsgHeader;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.msg.RespMsgBody;
import io.github.hylexus.jt808.msg.req.AuthRequestMsgBody;
import io.github.hylexus.jt808.msg.resp.CommonReplyMsgBody;
import io.github.hylexus.jt808.samples.annotation.entity.req.LocationUploadMsgBody;
import io.github.hylexus.jt808.session.Session;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hylexus
 * Created At 2020-02-01 2:54 下午
 */
@Slf4j
@Jt808RequestMsgHandler
public class CommonHandler {

    @Jt808RequestMsgMapping(msgType = 0x0102)
    public RespMsgBody processAuthMsg(AuthRequestMsgBody msgBody, RequestMsgHeader header) {
        log.info("处理鉴权消息 terminalId = {}, authCode = {}", header.getTerminalId(), msgBody.getAuthCode());
        return CommonReplyMsgBody.success(header.getFlowId(), BuiltinJt808MsgType.CLIENT_AUTH);
    }

    @Jt808RequestMsgMapping(msgType = 0x0200)
    public RespMsgBody processLocationMsg(
            Session session, RequestMsgMetadata metadata,
            RequestMsgHeader header, LocationUploadMsgBody msgBody) {

        assert header.getMsgId() == BuiltinJt808MsgType.CLIENT_LOCATION_INFO_UPLOAD.getMsgId();
        assert session.getTerminalId().equals(header.getTerminalId());
        assert session.getTerminalId().equals(metadata.getHeader().getTerminalId());
        assert metadata.getHeader() == header;

        log.info("处理位置上报消息 terminalId = {}, msgBody = {}", header.getTerminalId(), msgBody);
        return CommonReplyMsgBody.success(header.getFlowId(), BuiltinJt808MsgType.CLIENT_LOCATION_INFO_UPLOAD);
    }

}
