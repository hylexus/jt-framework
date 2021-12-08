package io.github.hylexus.jt.jt808.support.dispatcher.handler.scan;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt.exception.JtIllegalStateException;
import io.github.hylexus.jt.jt808.support.MsgTypeParser;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestMsgHandler;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestMsgHandlerMapping;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.reflection.HandlerMethod;
import io.github.hylexus.jt.jt808.support.dispatcher.impl.ComponentMapping;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.stream.Collectors;

import static io.github.hylexus.jt.jt808.support.utils.ReflectionUtils.isVoidReturnType;

public class Jt808RequestMsgHandlerScanner {
    private final ApplicationContext applicationContext;
    private final MsgTypeParser msgTypeParser;

    public Jt808RequestMsgHandlerScanner(ApplicationContext applicationContext, MsgTypeParser msgTypeParser) {
        this.applicationContext = applicationContext;
        this.msgTypeParser = msgTypeParser;
    }

    public void doScan(ComponentMapping<HandlerMethod> componentMapping) {
        final Set<Class<?>> handlerClassListFromSpringContainer = applicationContext
                .getBeansWithAnnotation(Jt808RequestMsgHandler.class)
                .values().stream().map(Object::getClass)
                .filter(this::isHandlerClass)
                .collect(Collectors.toSet());

        if (!handlerClassListFromSpringContainer.isEmpty()) {
            detectMsgHandler(componentMapping, handlerClassListFromSpringContainer);
        }
    }

    public void detectMsgHandler(ComponentMapping<HandlerMethod> componentMapping, Set<Class<?>> handlerClassList) {

        for (Class<?> cls : handlerClassList) {
            final Jt808RequestMsgHandler handlerAnnotation = AnnotationUtils.findAnnotation(cls, Jt808RequestMsgHandler.class);
            assert handlerAnnotation != null;

            final Method[] declaredMethods = ReflectionUtils.getAllDeclaredMethods(ClassUtils.getUserClass(cls));
            for (Method method : declaredMethods) {

                if (!isRequestMsgMappingMethod(method)) {
                    continue;
                }

                final Jt808RequestMsgHandlerMapping mappingAnnotation = method.getAnnotation(Jt808RequestMsgHandlerMapping.class);

                for (int msgId : mappingAnnotation.msgType()) {
                    MsgType msgType = msgTypeParser.parseMsgType(msgId)
                            .orElseThrow(() -> new JtIllegalStateException("Can not parse msgType with msgId " + msgId));
                    for (Jt808ProtocolVersion version : mappingAnnotation.versions()) {
                        final HandlerMethod handlerMethod = new HandlerMethod(
                                createBeanInstance(cls), method, isVoidReturnType(method),
                                Set.of(version), Set.of(msgType)
                        );
                        componentMapping.register(handlerMethod, msgType, version);
                    }
                }
            }
        }
    }


    private Object createBeanInstance(Class<?> cls) {
        return applicationContext.getBean(cls);
    }

    private boolean isRequestMsgMappingMethod(Method method) {
        return method.getAnnotation(Jt808RequestMsgHandlerMapping.class) != null;
    }

    private boolean isHandlerClass(Class<?> cls) {
        return AnnotationUtils.findAnnotation(cls, Jt808RequestMsgHandler.class) != null;
    }

}
