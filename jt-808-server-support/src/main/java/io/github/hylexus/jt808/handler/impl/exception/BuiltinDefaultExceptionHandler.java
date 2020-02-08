package io.github.hylexus.jt808.handler.impl.exception;

import io.github.hylexus.jt808.handler.ExceptionHandler;
import io.github.hylexus.jt808.msg.RequestMsgBody;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.msg.resp.VoidRespMsgBody;
import io.github.hylexus.jt808.session.Session;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Set;

/**
 * @author hylexus
 * Created At 2020-02-08 9:30 下午
 */
@Slf4j
public class BuiltinDefaultExceptionHandler implements ExceptionHandler {

    @Override
    public Set<Class<? extends Throwable>> getSupportedExceptionTypes() {
        return Collections.singleton(Throwable.class);
    }

    @Override
    public Object handleException(RequestMsgMetadata metadata, Session session, Object handlerMethod, RequestMsgBody msg, Throwable throwable) throws Throwable {
        log.error("", throwable);
        return VoidRespMsgBody.NO_DATA_WILL_BE_SENT_TO_CLIENT;
    }
}
