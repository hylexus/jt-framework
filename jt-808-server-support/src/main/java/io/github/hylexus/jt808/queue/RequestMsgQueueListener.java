package io.github.hylexus.jt808.queue;

import io.github.hylexus.jt808.msg.RequestMsgBody;
import io.github.hylexus.jt808.msg.RequestMsgCommonProps;

import java.io.IOException;

/**
 * @author hylexus
 * Created At 2019-08-25 18:29
 */
public interface RequestMsgQueueListener {

    void consumeMsg(RequestMsgCommonProps commonProps, RequestMsgBody body) throws IOException, InterruptedException;

}
