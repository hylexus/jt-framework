package io.github.hylexus.jt808.handler.impl;

import com.google.common.collect.Sets;
import io.github.hylexus.jt808.handler.AbstractMsgHandler;
import io.github.hylexus.jt808.msg.AbstractRequestMsg;
import io.github.hylexus.jt808.msg.BuiltinMsgType;
import io.github.hylexus.jt808.msg.RespMsgBody;
import io.github.hylexus.jt808.session.Session;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * 无需回复的消息处理器
 *
 * @author hylexus
 * Created At 2019-08-26 8:54 下午
 */
@Slf4j
public class NoReplyMsgHandler extends AbstractMsgHandler {

    public NoReplyMsgHandler() {
        super(Sets.newHashSet(BuiltinMsgType.CLIENT_COMMON_REPLY));
    }

    @Override
    protected Optional<RespMsgBody> doProcess(AbstractRequestMsg msg, Session session) {
        log.debug("No reply for {}", msg.getMsgType());
        return Optional.empty();
    }
}
