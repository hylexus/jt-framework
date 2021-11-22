package io.github.hylexus.jt808.samples.mixedversion.handler;

import io.github.hylexus.jt.annotation.msg.handler.Jt808RequestMsgHandler;
import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt808.handler.AbstractMsgHandler;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.msg.RespMsgBody;
import io.github.hylexus.jt808.samples.mixedversion.entity.req.RegisterRequestMsgV2019;
import io.github.hylexus.jt808.samples.mixedversion.entity.resp.RegisterRespMsgV2011;
import io.github.hylexus.jt808.session.Jt808Session;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.Set;

/**
 * @author hylexus
 * Created At 2019-09-19 11:31 下午
 */
@Slf4j
//@Component
@Jt808RequestMsgHandler(msgType = 0x0100)
public class RegisterMsgV2019MsgHandler extends AbstractMsgHandler<RegisterRequestMsgV2019> {

    @Override
    public Set<Jt808ProtocolVersion> getSupportedProtocolVersions() {
        return Jt808ProtocolVersion.unmodifiableSetVersion2019();
    }

    @Override
    protected Optional<RespMsgBody> doProcess(RequestMsgMetadata metadata, RegisterRequestMsgV2019 msg, Jt808Session session) {
        final RegisterRespMsgV2011 registerRespMsgV2011 = new RegisterRespMsgV2011(
                metadata.getHeader().getFlowId(),
                (byte) 0,
                "admin-654321"
        );
        return Optional.of(registerRespMsgV2011);
    }
}
