package io.github.hylexus.jt808.queue.listener;

import io.github.hylexus.jt.exception.JtSessionNotFoundException;
import io.github.hylexus.jt808.handler.MsgHandler;
import io.github.hylexus.jt808.msg.RequestMsgBody;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.queue.RequestMsgQueue;
import io.github.hylexus.jt808.queue.RequestMsgQueueListener;
import io.github.hylexus.jt808.session.Session;
import io.github.hylexus.jt808.session.SessionManager;
import io.github.hylexus.jt808.support.MsgHandlerMapping;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Optional;

/**
 * @author hylexus
 * Created At 2019-08-25 6:43 下午
 */
@Slf4j
public abstract class AbstractRequestMsgQueueListener<T extends RequestMsgQueue> implements RequestMsgQueueListener {
    protected MsgHandlerMapping msgHandlerMapping;
    protected T queue;

    public AbstractRequestMsgQueueListener(MsgHandlerMapping msgHandlerMapping, T queue) {
        this.msgHandlerMapping = msgHandlerMapping;
        this.queue = queue;
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void consumeMsg(RequestMsgMetadata metadata, RequestMsgBody body) throws IOException, InterruptedException {
        Optional<MsgHandler<? extends RequestMsgBody>> handlerInfo = msgHandlerMapping.getHandler(metadata.getMsgType());
        if (!handlerInfo.isPresent()) {
            log.error("No handler found for MsgType : {}", metadata.getMsgType());
            return;
        }

        MsgHandler<? extends RequestMsgBody> msgHandler = handlerInfo.get();
        ((MsgHandler) msgHandler).handleMsg(metadata, body, getSession(metadata));
    }

    protected Session getSession(RequestMsgMetadata metadata) {
        final String terminalId = metadata.getHeader().getTerminalId();
        Optional<Session> session = SessionManager.getInstance().findByTerminalId(terminalId);
        if (session.isPresent()) {
            return session.get();
        }

        throw new JtSessionNotFoundException(terminalId);
    }
}
