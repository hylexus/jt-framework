package io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver;

import io.github.hylexus.jt.jt808.support.dispatcher.handler.reflection.MethodParameter;
import io.github.hylexus.jt.jt808.support.exception.Jt808ArgumentResolveException;

/**
 * 抄袭自 org.springframework.web.method.support.HandlerMethodArgumentResolver
 *
 * @author hylexus
 */
public interface Jt808HandlerMethodArgumentResolver {

    boolean supportsParameter(MethodParameter methodParameter);

    Object resolveArgument(MethodParameter methodParameter, ArgumentContext context) throws Jt808ArgumentResolveException;

}
