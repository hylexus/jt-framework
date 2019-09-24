package io.github.hylexus.jt808.handler.impl;

import com.google.common.collect.Sets;
import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt808.handler.AbstractMsgHandler;
import io.github.hylexus.jt808.msg.*;
import io.github.hylexus.jt808.session.Session;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.Set;

import static java.util.Optional.of;

/**
 * @author hylexus
 * Created At 2019-08-26 8:46 下午
 */
@Slf4j
@BuiltinComponent
public class HeartBeatMsgHandler extends AbstractMsgHandler {

    @Override
    public Set<MsgType> getSupportedMsgTypes() {
        return Sets.newHashSet(BuiltinMsgType.CLIENT_HEART_BEAT);
    }

    @Override
    protected Optional<RespMsgBody> doProcess(RequestMsgMetadata metadata, RequestMsgBody msg, Session session) {
        log.debug("client heart beat, terminalId = {}", metadata.getHeader().getTerminalId());
        return of(commonReply(metadata, BuiltinMsgType.CLIENT_HEART_BEAT));
    }
}
