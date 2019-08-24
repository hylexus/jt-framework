package io.github.hylexus.jt.jt808.queue.listener;

import com.google.common.eventbus.Subscribe;
import io.github.hylexus.jt.exception.SessionNotFoundException;
import io.github.hylexus.jt.jt808.handler.MsgHandler;
import io.github.hylexus.jt.jt808.msg.AbstractRequestMsg;
import io.github.hylexus.jt.jt808.queue.impl.LocalEventBus;
import io.github.hylexus.jt.jt808.session.Session;
import io.github.hylexus.jt.jt808.session.SessionManager;
import io.github.hylexus.jt.jt808.support.MsgHandlerMapping;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Optional;

/**
 * @author hylexus
 * Created At 2019-08-24 16:44
 */
@Slf4j
public class LocalEventBusListener {

    private MsgHandlerMapping msgHandlerMapping;
    private LocalEventBus localEventBus;

    public LocalEventBusListener(MsgHandlerMapping msgHandlerMapping, LocalEventBus localEventBus) {
        this.msgHandlerMapping = msgHandlerMapping;
        this.localEventBus = localEventBus;
    }

    @PostConstruct
    public void init() {
        localEventBus.register(this);
    }

    @Subscribe
    public void listen(AbstractRequestMsg requestMsg) throws IOException, InterruptedException {
        Optional<MsgHandler> handler = msgHandlerMapping.getHandler(requestMsg.getMsgType());
        if (!handler.isPresent()) {
            log.error("No handler found for MsgType : {}", requestMsg.getMsgType());
        }

        handler.get().handleMsg(requestMsg, this.getSession(requestMsg));
    }

    private Session getSession(AbstractRequestMsg msg) {
        final String terminalId = msg.getHeader().getTerminalId();
        Optional<Session> session = SessionManager.getInstance().findByTerminalId(terminalId);
        if (session.isPresent()) {
            return session.get();
        }

        throw new SessionNotFoundException(terminalId);
    }
}
