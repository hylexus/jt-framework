package io.github.hylexus.jt808.queue.listener;

import com.google.common.eventbus.Subscribe;
import io.github.hylexus.jt808.handler.MsgHandler;
import io.github.hylexus.jt808.msg.AbstractRequestMsg;
import io.github.hylexus.jt808.queue.impl.LocalEventBus;
import io.github.hylexus.jt808.support.MsgHandlerMapping;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Optional;

/**
 * @author hylexus
 * Created At 2019-08-24 16:44
 */
@Slf4j
public class LocalEventBusListener extends AbstractRequestMsgQueueListener<LocalEventBus> {

    public LocalEventBusListener(MsgHandlerMapping msgHandlerMapping, LocalEventBus queue) {
        super(msgHandlerMapping, queue);
    }

    @PostConstruct
    public void init() {
        queue.register(this);
    }

    @Subscribe
    public void listen(AbstractRequestMsg requestMsg) throws IOException, InterruptedException {
        consumeMsg(requestMsg);
    }

    @Override
    public void consumeMsg(AbstractRequestMsg requestMsg) throws IOException, InterruptedException {
        Optional<MsgHandler> handler = msgHandlerMapping.getHandler(requestMsg.getMsgType());
        if (!handler.isPresent()) {
            log.error("No handler found for MsgType : {}", requestMsg.getMsgType());
        }

        handler.get().handleMsg(requestMsg, getSession(requestMsg));
    }
}
