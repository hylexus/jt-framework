package io.github.hylexus.jt808.utils;

import io.github.hylexus.jt808.handler.impl.reflection.HandlerMethod;
import io.github.hylexus.jt808.handler.impl.reflection.MethodParameter;
import io.github.hylexus.jt808.handler.impl.reflection.argument.resolver.HandlerMethodArgumentResolver;
import io.github.hylexus.jt808.msg.RequestMsgBody;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.session.Session;

/**
 * @author hylexus
 * Created At 2020-02-08 9:55 下午
 */
public class CommonUtils {

    public static Object[] resolveArguments(
            HandlerMethod handlerMethod, RequestMsgMetadata metadata, RequestMsgBody msg, Session session,
            HandlerMethodArgumentResolver argumentResolver) {

        final Object[] args = new Object[handlerMethod.getParameters().length];
        for (int i = 0; i < handlerMethod.getParameters().length; i++) {
            final MethodParameter parameter = handlerMethod.getParameters()[i];
            args[i] = argumentResolver.resolveArgument(parameter, metadata, session, msg);
        }
        return args;
    }

}
