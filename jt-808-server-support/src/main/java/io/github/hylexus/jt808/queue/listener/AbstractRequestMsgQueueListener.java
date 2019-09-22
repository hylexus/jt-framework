package io.github.hylexus.jt808.queue.listener;

import io.github.hylexus.jt.exception.SessionNotFoundException;
import io.github.hylexus.jt808.handler.MsgHandler;
import io.github.hylexus.jt808.msg.RequestMsgBody;
import io.github.hylexus.jt808.msg.RequestMsgCommonProps;
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
    @SuppressWarnings("unchecked")
    public void consumeMsg(RequestMsgCommonProps commonProps, RequestMsgBody body) throws IOException, InterruptedException {
        Optional<MsgHandler> handler = msgHandlerMapping.getHandler(commonProps.getMsgType());
        if (!handler.isPresent()) {
            log.error("No handler found for MsgType : {}", commonProps.getMsgType());
            return;
        }

        handler.get().handleMsg(commonProps, body, getSession(commonProps));
    }

    protected Session getSession(RequestMsgCommonProps commonProps) {
        final String terminalId = commonProps.getHeader().getTerminalId();
        Optional<Session> session = SessionManager.getInstance().findByTerminalId(terminalId);
        if (session.isPresent()) {
            return session.get();
        }

        throw new SessionNotFoundException(terminalId);
    }
}
