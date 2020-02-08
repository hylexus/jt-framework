package io.github.hylexus.jt808.handler.impl.exception;

import io.github.hylexus.jt808.handler.ExceptionHandler;
import io.github.hylexus.jt808.handler.impl.reflection.HandlerMethod;
import io.github.hylexus.jt808.handler.impl.reflection.argument.resolver.HandlerMethodArgumentResolver;
import io.github.hylexus.jt808.msg.RequestMsgBody;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.msg.resp.VoidRespMsgBody;
import io.github.hylexus.jt808.session.Session;
import io.github.hylexus.jt808.support.exception.scan.ExceptionHandlerMethod;
import io.github.hylexus.jt808.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author hylexus
 * Created At 2020-02-08 5:56 下午
 */
@Slf4j
public class ExceptionHandlerMethodExceptionHandler implements ExceptionHandler {

    private final ExceptionHandlerMethod exceptionHandlerMethod;
    private final HandlerMethodArgumentResolver argumentResolver;

    public ExceptionHandlerMethodExceptionHandler(ExceptionHandlerMethod exceptionHandlerMethod, HandlerMethodArgumentResolver argumentResolver) {
        this.exceptionHandlerMethod = exceptionHandlerMethod;
        this.argumentResolver = argumentResolver;
    }

    @Override
    public Set<Class<? extends Throwable>> getSupportedExceptionTypes() {
        return exceptionHandlerMethod.getSupportedExceptionTypes();
    }

    @Override
    public Object handleException(RequestMsgMetadata metadata, Session session, Object handlerMethod, RequestMsgBody msg, Throwable throwable) throws Throwable {
        final Object beanInstance = this.exceptionHandlerMethod.getBeanInstance();
        final Method method = this.exceptionHandlerMethod.getMethod();
        final Object[] args = this.resolveArgs(this.exceptionHandlerMethod, metadata, msg, session);

        if (this.exceptionHandlerMethod.isVoidReturnType()) {
            method.invoke(beanInstance, args);
            return VoidRespMsgBody.NO_DATA_WILL_BE_SENT_TO_CLIENT;
        }

        return method.invoke(beanInstance, args);
    }

    @Override
    public int getOrder() {
        return BUILTIN_COMPONENT_ORDER;
    }

    private Object[] resolveArgs(HandlerMethod handlerMethod, RequestMsgMetadata metadata, RequestMsgBody msg, Session session) {
        return CommonUtils.resolveArguments(handlerMethod, metadata, msg, session, this.argumentResolver);
    }
}
