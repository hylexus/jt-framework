package io.github.hylexus.jt.jt808.support.dispatcher.handler.exception.handler;

import io.github.hylexus.jt.exception.JtIllegalStateException;
import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.MsgType;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808ExceptionHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.Jt808HandlerResult;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver.ArgumentContext;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver.Jt808HandlerMethodArgumentResolver;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.reflection.HandlerMethod;
import io.github.hylexus.jt.jt808.support.utils.ArgumentUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author hylexus
 * @see Jt808ExceptionHandler
 * @see io.github.hylexus.jt.jt808.support.annotation.handler.Jt808ExceptionHandler
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Slf4j
public class ExceptionHandlerHandlerMethod extends HandlerMethod implements Jt808ExceptionHandler {

    private final Set<Class<? extends Throwable>> supportedExceptionTypes;
    private final Jt808HandlerMethodArgumentResolver argumentResolver;

    public ExceptionHandlerHandlerMethod(
            Object beanInstance, Method method, boolean isVoidReturnType,
            Set<Class<? extends Throwable>> supportedExceptionTypes,
            Jt808HandlerMethodArgumentResolver argumentResolver, int order) {

        super(beanInstance, method, isVoidReturnType, null, null, order);
        this.supportedExceptionTypes = supportedExceptionTypes;
        this.argumentResolver = argumentResolver;
    }

    @Override
    public Set<Jt808ProtocolVersion> getSupportedVersions() {
        throw new JtIllegalStateException();
    }

    @Override
    public Set<MsgType> getSupportedMsgTypes() {
        throw new JtIllegalStateException();
    }

    @Override
    public Set<Class<? extends Throwable>> getSupportedExceptionTypes() {
        return supportedExceptionTypes;
    }

    @Nonnull
    @Override
    public Jt808HandlerResult handleException(Object handler, ArgumentContext argumentContext) throws Throwable {
        final Object beanInstance = this.getBeanInstance();
        final Method method = this.getMethod();
        final Object[] args = this.resolveArgs(this, argumentContext);

        final Object invokeResult = method.invoke(beanInstance, args);
        if (this.isVoidReturnType()) {
            return Jt808HandlerResult.empty();
        }
        return new Jt808HandlerResult().setHandler(this).setReturnValue(invokeResult);
    }

    private Object[] resolveArgs(HandlerMethod handlerMethod, ArgumentContext argumentContext) {
        return ArgumentUtils.resolveArguments(handlerMethod, argumentContext, this.argumentResolver);
    }
}
