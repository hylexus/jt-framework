package io.github.hylexus.jt808.handler.impl;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.data.msg.BuiltinJt808MsgType;
import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt808.handler.AbstractMsgHandler;
import io.github.hylexus.jt808.msg.RequestMsgBody;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.msg.RespMsgBody;
import io.github.hylexus.jt808.session.Jt808Session;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static java.util.Optional.of;

/**
 * @author hylexus
 * Created At 2019-08-26 8:46 下午
 */
@Slf4j
@BuiltinComponent
public class BuiltinHeartBeatMsgHandler extends AbstractMsgHandler<RequestMsgBody> {

    @Override
    public int getOrder() {
        return BUILTIN_COMPONENT_ORDER;
    }

    @Override
    public Set<MsgType> getSupportedMsgTypes() {
        return Collections.singleton(BuiltinJt808MsgType.CLIENT_HEART_BEAT);
    }

    @Override
    protected Optional<RespMsgBody> doProcess(RequestMsgMetadata metadata, RequestMsgBody msg, Jt808Session session) {
        log.debug("client heart beat, terminalId = {}", metadata.getHeader().getTerminalId());
        return of(commonSuccessReply(metadata, BuiltinJt808MsgType.CLIENT_HEART_BEAT));
    }
}
