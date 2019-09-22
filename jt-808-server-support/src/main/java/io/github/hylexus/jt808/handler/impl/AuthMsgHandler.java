package io.github.hylexus.jt808.handler.impl;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt808.ext.AuthCodeValidator;
import io.github.hylexus.jt808.handler.AbstractMsgHandler;
import io.github.hylexus.jt808.msg.BuiltinMsgType;
import io.github.hylexus.jt808.msg.MsgType;
import io.github.hylexus.jt808.msg.RequestMsgCommonProps;
import io.github.hylexus.jt808.msg.RespMsgBody;
import io.github.hylexus.jt808.msg.req.AuthRequestMsgBody;
import io.github.hylexus.jt808.session.Session;

import java.util.Optional;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static io.github.hylexus.jt808.msg.RespMsgBody.AUTH_CODE_ERROR;
import static java.util.Optional.of;

/**
 * @author hylexus
 * Created At 2019-08-24 15:44
 */
@BuiltinComponent
public class AuthMsgHandler extends AbstractMsgHandler<AuthRequestMsgBody> {

    private AuthCodeValidator authCodeValidator;

    @Override
    public Set<MsgType> getSupportedMsgTypes() {
        return newHashSet(BuiltinMsgType.CLIENT_AUTH);
    }

    public AuthMsgHandler(AuthCodeValidator authCodeValidator) {
        this.authCodeValidator = authCodeValidator;
    }

    @Override
    protected Optional<RespMsgBody> doProcess(RequestMsgCommonProps props, AuthRequestMsgBody body, Session session) {
        boolean valid = authCodeValidator.validateAuthCode(session, props, body);
        if (valid) {
            return of(commonReply(props, BuiltinMsgType.CLIENT_AUTH));
        }
        return of(generateCommonReplyMsgBody(props, BuiltinMsgType.CLIENT_AUTH, AUTH_CODE_ERROR));
    }
}
