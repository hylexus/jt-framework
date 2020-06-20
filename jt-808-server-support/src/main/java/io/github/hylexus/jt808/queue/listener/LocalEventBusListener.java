package io.github.hylexus.jt808.queue.listener;

import com.google.common.eventbus.Subscribe;
import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt808.codec.Encoder;
import io.github.hylexus.jt808.converter.ResponseMsgBodyConverter;
import io.github.hylexus.jt808.handler.ExceptionHandler;
import io.github.hylexus.jt808.msg.RequestMsgWrapper;
import io.github.hylexus.jt808.queue.impl.LocalEventBus;
import io.github.hylexus.jt808.session.Jt808SessionManager;
import io.github.hylexus.jt808.support.MsgHandlerMapping;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * @author hylexus
 * Created At 2019-08-24 16:44
 */
@Slf4j
@BuiltinComponent
public class LocalEventBusListener extends AbstractRequestMsgQueueListener<LocalEventBus> {


    public LocalEventBusListener(
            MsgHandlerMapping msgHandlerMapping, LocalEventBus queue, ExceptionHandler exceptionHandler,
            ResponseMsgBodyConverter responseMsgBodyConverter, Encoder encoder,
            Jt808SessionManager sessionManager) {

        super(msgHandlerMapping, queue, exceptionHandler, responseMsgBodyConverter, encoder, sessionManager);
    }

    @PostConstruct
    public void init() {
        //noinspection UnstableApiUsage
        queue.register(this);
    }

    @Subscribe
    public void listen(RequestMsgWrapper wrapper) throws IOException, InterruptedException {
        consumeMsg(wrapper.getMetadata(), wrapper.getBody());
    }

}
