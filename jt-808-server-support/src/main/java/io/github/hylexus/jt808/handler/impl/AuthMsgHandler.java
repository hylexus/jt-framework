package io.github.hylexus.jt808.handler.impl;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.data.msg.BuiltinJt808MsgType;
import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt808.ext.AuthCodeValidator;
import io.github.hylexus.jt808.handler.AbstractMsgHandler;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.msg.RespMsgBody;
import io.github.hylexus.jt808.msg.req.AuthRequestMsgBody;
import io.github.hylexus.jt808.session.Session;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static io.github.hylexus.jt808.msg.RespMsgBody.AUTH_CODE_ERROR;
import static java.util.Optional.of;

/**
 * @author hylexus
 * Created At 2019-08-24 15:44
 */
@Slf4j
@BuiltinComponent
public class AuthMsgHandler extends AbstractMsgHandler<AuthRequestMsgBody> {

    private AuthCodeValidator authCodeValidator;

    @Override
    public Set<MsgType> getSupportedMsgTypes() {
        return newHashSet(BuiltinJt808MsgType.CLIENT_AUTH);
    }

    public AuthMsgHandler(AuthCodeValidator authCodeValidator) {
        this.authCodeValidator = authCodeValidator;
    }

    @Override
    protected Optional<RespMsgBody> doProcess(RequestMsgMetadata metadata, AuthRequestMsgBody body, Session session) {
        log.debug("receive AuthMsg : {}", body);
        boolean valid = authCodeValidator.validateAuthCode(session, metadata, body);
        if (valid) {
            return of(commonReply(metadata, BuiltinJt808MsgType.CLIENT_AUTH));
        }
        return of(generateCommonReplyMsgBody(metadata, BuiltinJt808MsgType.CLIENT_AUTH, AUTH_CODE_ERROR));
    }
}
