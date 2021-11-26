package io.github.hylexus.jt808.samples.mixedversion.handler;

import io.github.hylexus.jt.annotation.msg.handler.Jt808RequestMsgHandler;
import io.github.hylexus.jt.annotation.msg.handler.Jt808RequestMsgHandlerMapping;
import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.data.msg.BuiltinJt808MsgType;
import io.github.hylexus.jt808.msg.RequestMsgHeader;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.msg.resp.BuiltinServerCommonReplyRespMsgBody;
import io.github.hylexus.jt808.samples.mixedversion.config.Jt808MsgType;
import io.github.hylexus.jt808.samples.mixedversion.entity.req.*;
import io.github.hylexus.jt808.session.Jt808Session;
import io.github.hylexus.jt808.session.Jt808SessionManager;
import io.github.hylexus.jt808.session.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static io.github.hylexus.jt808.samples.mixedversion.config.Jt808MsgType.CLIENT_AUTH;
import static io.github.hylexus.jt808.samples.mixedversion.config.Jt808MsgType.CLIENT_LOCATION_INFO_UPLOAD;

/**
 * @author hylexus
 * Created At 2020-02-01 2:54 下午
 */
@Slf4j
@Jt808RequestMsgHandler
@Component
public class CommonHandler {

    @Autowired
    private Jt808SessionManager jt808SessionManager;

    @Jt808RequestMsgHandlerMapping(msgType = 0x0701, versions = Jt808ProtocolVersion.VERSION_2011)
    public void process0701(ReqMsg0701 msg0701) {
        log.info("{}", msg0701);
    }

    // 此处会覆盖内置的鉴权消息处理器(如果启用了的话)
    @Jt808RequestMsgHandlerMapping(msgType = 0x0102, versions = Jt808ProtocolVersion.VERSION_2011, desc = "终端鉴权")
    public BuiltinServerCommonReplyRespMsgBody processAuthMsgV2011(
            AuthRequestMsgV2011 msgBody, RequestMsgHeader header, Jt808Session abstractSession, Session session) {

        log.info("处理鉴权消息 terminalId = {}, authCode = {}", header.getTerminalId(), msgBody.getAuthCode());
        if (header.getTerminalId().equals(System.getProperty("debug-terminal-id"))) {
            throw new UnsupportedOperationException("terminal [" + header.getTerminalId() + "] was locked.");
        }
        Optional<Jt808Session> sessionInfo = jt808SessionManager.findByTerminalId(header.getTerminalId());
        assert sessionInfo.isPresent();
        assert sessionInfo.get() == abstractSession;
        // 不建议直接使用Session，建议使用Jt808Session
        assert sessionInfo.get() == session;
        // return CommonReplyMsgBody.success(header.getFlowId(), BuiltinJt808MsgType.CLIENT_AUTH);
        return new BuiltinServerCommonReplyRespMsgBody(header.getFlowId(), CLIENT_AUTH.getMsgId(), (byte) 0);
    }

    // 此处会覆盖内置的鉴权消息处理器(如果启用了的话)
    @Jt808RequestMsgHandlerMapping(msgType = 0x0102, versions = Jt808ProtocolVersion.VERSION_2019, desc = "终端鉴权")
    public BuiltinServerCommonReplyRespMsgBody processAuthMsgV2019(
            AuthRequestMsgV2019 msgBody, RequestMsgHeader header, Jt808Session abstractSession, Session session) {
        log.info("处理鉴权消息 terminalId = {}, authCode = {}", header.getTerminalId(), msgBody.getAuthCode());
        if (header.getTerminalId().equals(System.getProperty("debug-terminal-id"))) {
            throw new UnsupportedOperationException("terminal [" + header.getTerminalId() + "] was locked.");
        }
        Optional<Jt808Session> sessionInfo = jt808SessionManager.findByTerminalId(header.getTerminalId());
        assert sessionInfo.isPresent();
        assert sessionInfo.get() == abstractSession;
        // 不建议直接使用Session，建议使用Jt808Session
        assert sessionInfo.get() == session;
        // return CommonReplyMsgBody.success(header.getFlowId(), BuiltinJt808MsgType.CLIENT_AUTH);
        return new BuiltinServerCommonReplyRespMsgBody(header.getFlowId(), CLIENT_AUTH.getMsgId(), (byte) 0);
    }

    // 处理MsgId为0x0200的消息
    @Jt808RequestMsgHandlerMapping(msgType = 0x0200, versions = Jt808ProtocolVersion.VERSION_2011)
    public BuiltinServerCommonReplyRespMsgBody processLocationMsgV2011(
            Jt808Session session, RequestMsgMetadata metadata,
            RequestMsgHeader header,
            LocationUploadReqMsgV2011 msgBody) {

        assert header.getMsgId() == BuiltinJt808MsgType.CLIENT_LOCATION_INFO_UPLOAD.getMsgId();
        assert session.getTerminalId().equals(header.getTerminalId());
        assert session.getTerminalId().equals(metadata.getHeader().getTerminalId());
        assert metadata.getHeader() == header;

        log.info("处理位置上报消息 terminalId = {}, msgBody = {}", header.getTerminalId(), msgBody);
        // return CommonReplyMsgBody.success(header.getFlowId(), BuiltinJt808MsgType.CLIENT_LOCATION_INFO_UPLOAD);
        return new BuiltinServerCommonReplyRespMsgBody(header.getFlowId(), CLIENT_LOCATION_INFO_UPLOAD.getMsgId(), (byte) 0);
    }

    // 处理MsgId为0x0200的消息
    @Jt808RequestMsgHandlerMapping(msgType = 0x0200, versions = Jt808ProtocolVersion.VERSION_2019)
    public BuiltinServerCommonReplyRespMsgBody processLocationMsgV2019(
            Jt808Session session, RequestMsgMetadata metadata,
            RequestMsgHeader header,
            LocationUploadReqMsgV2019 msgBody) {

        assert header.getMsgId() == BuiltinJt808MsgType.CLIENT_LOCATION_INFO_UPLOAD.getMsgId();
        assert session.getTerminalId().equals(header.getTerminalId());
        assert session.getTerminalId().equals(metadata.getHeader().getTerminalId());
        assert metadata.getHeader() == header;

        log.info("处理位置上报消息 terminalId = {}, msgBody = {}", header.getTerminalId(), msgBody);
        // return CommonReplyMsgBody.success(header.getFlowId(), BuiltinJt808MsgType.CLIENT_LOCATION_INFO_UPLOAD);
        return new BuiltinServerCommonReplyRespMsgBody(header.getFlowId(), CLIENT_LOCATION_INFO_UPLOAD.getMsgId(), (byte) 0);
    }

    @Jt808RequestMsgHandlerMapping(msgType = 0x0704, versions = Jt808ProtocolVersion.VERSION_2019)
    public BuiltinServerCommonReplyRespMsgBody processReqMsg0704(ReqMsg0704 msg0704, RequestMsgHeader header) {
        System.out.println(msg0704);
        return new BuiltinServerCommonReplyRespMsgBody(header.getFlowId(), Jt808MsgType.REQ_0704.getMsgId(), (byte) 0);
    }

}
