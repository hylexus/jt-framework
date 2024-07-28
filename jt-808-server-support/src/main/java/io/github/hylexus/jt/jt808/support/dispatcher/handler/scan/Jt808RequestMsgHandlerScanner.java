package io.github.hylexus.jt.jt808.support.dispatcher.handler.scan;

import io.github.hylexus.jt.core.BuiltinComponent;
import io.github.hylexus.jt.core.OrderedComponent;
import io.github.hylexus.jt.exception.JtIllegalArgumentException;
import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.Jt808MsgTypeParser;
import io.github.hylexus.jt.jt808.spec.MsgType;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestHandler;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestHandlerMapping;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.reflection.HandlerMethod;
import io.github.hylexus.jt.jt808.support.dispatcher.impl.ComponentMapping;
import io.github.hylexus.jt.utils.Jdk8Adapter;
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
    private final Jt808MsgTypeParser msgTypeParser;

    public Jt808RequestMsgHandlerScanner(ApplicationContext applicationContext, Jt808MsgTypeParser msgTypeParser) {
        this.applicationContext = applicationContext;
        this.msgTypeParser = msgTypeParser;
    }

    public void doScan(ComponentMapping<HandlerMethod> componentMapping) {
        final Set<Class<?>> handlerClassListFromSpringContainer = applicationContext
                .getBeansWithAnnotation(Jt808RequestHandler.class)
                .values().stream().map(Object::getClass)
                .filter(this::isHandlerClass)
                .collect(Collectors.toSet());

        if (!handlerClassListFromSpringContainer.isEmpty()) {
            detectMsgHandler(componentMapping, handlerClassListFromSpringContainer);
        }
    }

    public void detectMsgHandler(ComponentMapping<HandlerMethod> componentMapping, Set<Class<?>> handlerClassList) {

        for (Class<?> cls : handlerClassList) {
            final Jt808RequestHandler handlerAnnotation = AnnotationUtils.findAnnotation(cls, Jt808RequestHandler.class);
            assert handlerAnnotation != null;

            final Method[] declaredMethods = ReflectionUtils.getAllDeclaredMethods(ClassUtils.getUserClass(cls));
            final Object beanInstance = createBeanInstance(cls);
            final int order;
            if (beanInstance instanceof OrderedComponent) {
                order = ((OrderedComponent) beanInstance).getOrder();
            } else if (beanInstance instanceof BuiltinComponent) {
                order = OrderedComponent.LOWEST_PRECEDENCE;
            } else {
                final io.github.hylexus.jt.annotation.BuiltinComponent annotation =
                        AnnotationUtils.findAnnotation(cls, io.github.hylexus.jt.annotation.BuiltinComponent.class);
                if (annotation != null) {
                    order = annotation.order();
                } else {
                    order = OrderedComponent.DEFAULT_ORDER;
                }
            }
            for (Method method : declaredMethods) {

                if (!isRequestMsgMappingMethod(method)) {
                    continue;
                }

                final Jt808RequestHandlerMapping mappingAnnotation = method.getAnnotation(Jt808RequestHandlerMapping.class);

                for (int msgId : mappingAnnotation.msgType()) {
                    final MsgType msgType = msgTypeParser.parseMsgType(msgId)
                            .orElseThrow(() -> new JtIllegalArgumentException("Can not parse msgType with msgId " + msgId));
                    for (Jt808ProtocolVersion version : mappingAnnotation.versions()) {
                        final HandlerMethod handlerMethod = new HandlerMethod(
                                beanInstance, method, isVoidReturnType(method),
                                Jdk8Adapter.setOf(version), Jdk8Adapter.setOf(msgType), order
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
        return method.getAnnotation(Jt808RequestHandlerMapping.class) != null;
    }

    private boolean isHandlerClass(Class<?> cls) {
        return AnnotationUtils.findAnnotation(cls, Jt808RequestHandler.class) != null;
    }

}
