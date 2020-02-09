package io.github.hylexus.jt808.handler.impl.exception;

import io.github.hylexus.jt808.handler.ExceptionHandler;

/**
 * @author hylexus
 * Created At 2020-02-09 3:00 下午
 */
public abstract class AbstractBuiltinExceptionHandler implements ExceptionHandler {

    @Override
    public int getOrder() {
        return BUILTIN_COMPONENT_ORDER;
    }

}
