package io.github.hylexus.jt808.handler.impl;

import com.google.common.collect.Sets;
import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt808.handler.AbstractMsgHandler;
import io.github.hylexus.jt808.msg.*;
import io.github.hylexus.jt808.session.Session;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.Set;

/**
 * 无需回复的消息处理器
 *
 * @author hylexus
 * Created At 2019-08-26 8:54 下午
 */
@Slf4j
@BuiltinComponent
public class NoReplyMsgHandler extends AbstractMsgHandler {

    @Override
    public Set<MsgType> getSupportedMsgTypes() {
        return Sets.newHashSet(BuiltinMsgType.CLIENT_COMMON_REPLY);
    }

    @Override
    protected Optional<RespMsgBody> doProcess(RequestMsgCommonProps commonProps, RequestMsgBody msg, Session session) {
        log.debug("No reply for {}", commonProps.getMsgType());
        return Optional.empty();
    }
}
