package io.github.hylexus.jt808.queue.listener;

import io.github.hylexus.jt.exception.SessionNotFoundException;
import io.github.hylexus.jt808.msg.AbstractRequestMsg;
import io.github.hylexus.jt808.queue.RequestMsgQueue;
import io.github.hylexus.jt808.queue.RequestMsgQueueListener;
import io.github.hylexus.jt808.session.Session;
import io.github.hylexus.jt808.session.SessionManager;
import io.github.hylexus.jt808.support.MsgHandlerMapping;

import java.util.Optional;

/**
 * @author hylexus
 * Created At 2019-08-25 6:43 下午
 */
public abstract class AbstractRequestMsgQueueListener<T extends RequestMsgQueue> implements RequestMsgQueueListener {
    protected MsgHandlerMapping msgHandlerMapping;
    protected T queue;

    public AbstractRequestMsgQueueListener(MsgHandlerMapping msgHandlerMapping, T queue) {
        this.msgHandlerMapping = msgHandlerMapping;
        this.queue = queue;
    }

    protected Session getSession(AbstractRequestMsg msg) {
        final String terminalId = msg.getHeader().getTerminalId();
        Optional<Session> session = SessionManager.getInstance().findByTerminalId(terminalId);
        if (session.isPresent()) {
            return session.get();
        }

        throw new SessionNotFoundException(terminalId);
    }
}
