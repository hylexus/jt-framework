package io.github.hylexus.jt.jt808.dispatcher;

import io.github.hylexus.jt.jt808.msg.AbstractRequestMsg;
import io.github.hylexus.jt.jt808.queue.LocalMsgQueue;
import io.github.hylexus.jt.jt808.support.MsgConverterMapping;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author hylexus
 * createdAt 2019/1/28
 **/
@Slf4j
public class LocalDispatcher extends AbstractRequestMsgDispatcher {

    private LocalMsgQueue msgQueue;

    public LocalDispatcher(MsgConverterMapping msgConverterMapping) {
        super(msgConverterMapping);
        msgQueue = LocalMsgQueue.getInstance();
    }

    @Override
    public void doBroadcast(AbstractRequestMsg msg) throws Exception {
        msgQueue.postMsg(msg, 2, TimeUnit.SECONDS);
    }
}
