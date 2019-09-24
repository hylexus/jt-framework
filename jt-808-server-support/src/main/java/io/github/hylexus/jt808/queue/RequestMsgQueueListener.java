package io.github.hylexus.jt808.queue;

import io.github.hylexus.jt808.msg.RequestMsgBody;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;

import java.io.IOException;

/**
 * @author hylexus
 * Created At 2019-08-25 18:29
 */
public interface RequestMsgQueueListener {

    void consumeMsg(RequestMsgMetadata metadata, RequestMsgBody body) throws IOException, InterruptedException;

}
