package io.github.hylexus.jt808.handler.impl;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.data.msg.BuiltinJt808MsgType;
import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt808.ext.AuthCodeValidator;
import io.github.hylexus.jt808.handler.AbstractBuiltinRequestMsgHandler;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.msg.RespMsgBody;
import io.github.hylexus.jt808.msg.req.BuiltinAuthRequestMsgV2011;
import io.github.hylexus.jt808.msg.resp.CommonReplyMsgBody;
import io.github.hylexus.jt808.session.Jt808Session;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static io.github.hylexus.jt808.msg.RespMsgBody.AUTH_CODE_ERROR;
import static java.util.Optional.of;

/**
 * @author hylexus
 * Created At 2019-08-24 15:44
 */
@Slf4j
@BuiltinComponent
public class BuiltinAuthRequestMsgV2011HandlerForDebugging extends AbstractBuiltinRequestMsgHandler<BuiltinAuthRequestMsgV2011> {

    private final AuthCodeValidator authCodeValidator;

    public BuiltinAuthRequestMsgV2011HandlerForDebugging(AuthCodeValidator authCodeValidator) {
        this.authCodeValidator = authCodeValidator;
    }

    @Override
    public Set<Jt808ProtocolVersion> getSupportedProtocolVersions() {
        return Jt808ProtocolVersion.unmodifiableSetVersion2011();
    }

    @Override
    public Set<MsgType> getSupportedMsgTypes() {
        return Collections.singleton(BuiltinJt808MsgType.CLIENT_AUTH);
    }

    @Override
    public int getOrder() {
        return BUILTIN_COMPONENT_ORDER;
    }

    @Override
    protected Optional<RespMsgBody> doProcess(RequestMsgMetadata metadata, BuiltinAuthRequestMsgV2011 body, Jt808Session session) {
        log.debug("receive AuthMsg : {}", body);
        boolean valid = authCodeValidator.validateAuthCode(session, metadata, body);
        if (valid) {
            return of(commonSuccessReply(metadata, BuiltinJt808MsgType.CLIENT_AUTH));
        }
        return of(CommonReplyMsgBody.of(AUTH_CODE_ERROR, metadata.getHeader().getFlowId(), BuiltinJt808MsgType.CLIENT_AUTH));
    }
}
