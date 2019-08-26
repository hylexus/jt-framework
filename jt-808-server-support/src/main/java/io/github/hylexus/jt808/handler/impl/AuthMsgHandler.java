package io.github.hylexus.jt808.handler.impl;

import io.github.hylexus.jt808.ext.AuthCodeValidator;
import io.github.hylexus.jt808.handler.AbstractMsgHandler;
import io.github.hylexus.jt808.msg.BuiltinMsgType;
import io.github.hylexus.jt808.msg.RespMsgBody;
import io.github.hylexus.jt808.msg.req.AuthRequestMsg;
import io.github.hylexus.jt808.session.Session;

import java.util.Optional;

import static com.google.common.collect.Sets.newHashSet;
import static io.github.hylexus.jt808.msg.RespMsgBody.AUTH_CODE_ERROR;
import static java.util.Optional.of;

/**
 * @author hylexus
 * Created At 2019-08-24 15:44
 */
public class AuthMsgHandler extends AbstractMsgHandler<AuthRequestMsg> {

    private AuthCodeValidator authCodeValidator;

    public AuthMsgHandler(AuthCodeValidator authCodeValidator) {
        super(newHashSet(BuiltinMsgType.CLIENT_AUTH));
        this.authCodeValidator = authCodeValidator;
    }

    @Override
    protected Optional<RespMsgBody> doProcess(AuthRequestMsg requestMsg, Session session) {
        boolean valid = authCodeValidator.validateAuthCode(requestMsg);
        if (valid) {
            return of(commonReply(requestMsg, BuiltinMsgType.CLIENT_AUTH));
        }
        return of(generateCommonReplyMsgBody(requestMsg, BuiltinMsgType.CLIENT_AUTH, AUTH_CODE_ERROR));
    }
}
