package io.github.hylexus.jt808.handler.impl.exception;

import io.github.hylexus.jt808.handler.impl.reflection.argument.resolver.impl.ArgumentContext;
import io.github.hylexus.jt808.msg.resp.VoidRespMsgBody;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Set;

/**
 * @author hylexus
 * Created At 2020-02-08 9:30 下午
 */
@Slf4j
public class BuiltinLoggingExceptionHandler extends AbstractBuiltinExceptionHandler {

    @Override
    public Set<Class<? extends Throwable>> getSupportedExceptionTypes() {
        return Collections.singleton(Throwable.class);
    }

    @Override
    public Object handleException(Object handler, ArgumentContext argumentContext) throws Throwable {
        log.error("BuiltinLoggingExceptionHandler, handler = {}", handler, argumentContext.getThrowable());
        return VoidRespMsgBody.NO_DATA_WILL_BE_SENT_TO_CLIENT;
    }
}
