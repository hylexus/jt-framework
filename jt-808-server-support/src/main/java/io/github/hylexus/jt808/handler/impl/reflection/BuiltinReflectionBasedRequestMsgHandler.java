package io.github.hylexus.jt808.handler.impl.reflection;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt808.converter.ResponseMsgBodyConverter;
import io.github.hylexus.jt808.handler.ExceptionHandler;
import io.github.hylexus.jt808.handler.impl.reflection.argument.resolver.HandlerMethodArgumentResolver;

/**
 * @author hylexus
 * Created At 2020-02-02 2:05 下午
 */
@BuiltinComponent
public class BuiltinReflectionBasedRequestMsgHandler extends CustomReflectionBasedRequestMsgHandler {

    public BuiltinReflectionBasedRequestMsgHandler(
            HandlerMethodArgumentResolver argumentResolver, ResponseMsgBodyConverter responseMsgBodyConverter, ExceptionHandler exceptionHandler) {

        super(argumentResolver, responseMsgBodyConverter, exceptionHandler);
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}
