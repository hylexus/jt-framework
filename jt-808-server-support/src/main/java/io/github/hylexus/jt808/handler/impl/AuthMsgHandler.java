package io.github.hylexus.jt808.handler.impl;

import io.github.hylexus.jt808.handler.AbstractMsgHandler;
import io.github.hylexus.jt808.msg.BuiltinMsgType;
import io.github.hylexus.jt808.msg.RespMsgBody;
import io.github.hylexus.jt808.msg.req.AuthRequestMsg;
import io.github.hylexus.jt808.session.Session;

import java.util.Optional;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.Optional.of;

/**
 * @author hylexus
 * Created At 2019-08-24 15:44
 */
public class AuthMsgHandler extends AbstractMsgHandler<AuthRequestMsg> {

    public AuthMsgHandler() {
        super(newHashSet(BuiltinMsgType.REQ_CLIENT_AUTH));
    }

    @Override
    protected Optional<RespMsgBody> doProcess(AuthRequestMsg requestMsg, Session session) {
        return of(commonReply(requestMsg, BuiltinMsgType.REQ_CLIENT_AUTH));
    }
}
