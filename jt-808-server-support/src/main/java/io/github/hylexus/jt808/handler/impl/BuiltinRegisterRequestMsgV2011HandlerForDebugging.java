package io.github.hylexus.jt808.handler.impl;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.annotation.msg.handler.Jt808RequestMsgHandler;
import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt808.handler.AbstractBuiltinRequestMsgHandler;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.msg.RespMsgBody;
import io.github.hylexus.jt808.msg.req.BuiltinRegisterRequestMsgV2011;
import io.github.hylexus.jt808.msg.resp.BuiltinRegisterRespMsg;
import io.github.hylexus.jt808.session.Jt808Session;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.Set;

/**
 * @author hylexus
 * Created At 2019-09-19 11:31 下午
 */
@Slf4j
@BuiltinComponent
@Jt808RequestMsgHandler(msgType = 0x0100)
public class BuiltinRegisterRequestMsgV2011HandlerForDebugging extends AbstractBuiltinRequestMsgHandler<BuiltinRegisterRequestMsgV2011> {

    @Override
    public Set<Jt808ProtocolVersion> getSupportedProtocolVersions() {
        return Jt808ProtocolVersion.unmodifiableSetVersion2011();
    }

    @Override
    protected Optional<RespMsgBody> doProcess(RequestMsgMetadata metadata, BuiltinRegisterRequestMsgV2011 msg, Jt808Session session) {
        final BuiltinRegisterRespMsg registerRespMsgV2011 = new BuiltinRegisterRespMsg(
                metadata.getHeader().getFlowId(),
                (byte) 0,
                "12345"
        );
        return Optional.of(registerRespMsgV2011);
    }
}
