package io.github.hylexus.jt808.support.handler.scan;

import com.google.common.collect.Lists;
import io.github.hylexus.jt.annotation.msg.handler.Jt808RequestMsgHandler;
import io.github.hylexus.jt.annotation.msg.handler.Jt808RequestMsgMapping;
import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt.exception.JtIllegalArgumentException;
import io.github.hylexus.jt.spring.utils.ClassScanner;
import io.github.hylexus.jt808.converter.MsgTypeParser;
import io.github.hylexus.jt808.handler.impl.reflection.HandlerMethod;
import io.github.hylexus.jt808.handler.impl.reflection.MethodParameter;
import io.github.hylexus.jt808.handler.impl.reflection.ReflectionBasedRequestMsgHandler;
import io.github.hylexus.jt808.handler.impl.reflection.argument.resolver.HandlerMethodArgumentResolver;
import io.github.hylexus.jt808.support.MsgHandlerMapping;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Set;

/**
 * @author hylexus
 * Created At 2020-02-01 3:31 下午
 */
@Slf4j
public class Jt808MsgHandlerScanner implements InitializingBean {

    private final Set<String> packagesToScan;
    private final MsgTypeParser msgTypeParser;
    private final MsgHandlerMapping msgHandlerMapping;
    private final HandlerMethodArgumentResolver argumentResolver;

    public Jt808MsgHandlerScanner(Set<String> packagesToScan, MsgTypeParser msgTypeParser, MsgHandlerMapping msgHandlerMapping, HandlerMethodArgumentResolver argumentResolver) {
        this.packagesToScan = packagesToScan;
        this.msgTypeParser = msgTypeParser;
        this.msgHandlerMapping = msgHandlerMapping;
        this.argumentResolver = argumentResolver;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (CollectionUtils.isEmpty(packagesToScan)) {
            log.info("[jt808.handler-scan.base-packages] is empty. Skip...");
            return;
        }

        final ClassScanner scanner = new ClassScanner();
        @SuppressWarnings("rawtypes") final Set<Class> handlerClassList = scanner.doScan(packagesToScan, this::isHandlerClass);
        if (CollectionUtils.isEmpty(handlerClassList)) {
            log.info("No MsgBodyEntity found for Jt808");
            return;
        }

        final ReflectionBasedRequestMsgHandler defaultHandler = new ReflectionBasedRequestMsgHandler(argumentResolver);
        for (Class<?> cls : handlerClassList) {
            final Jt808RequestMsgHandler handlerAnnotation = AnnotationUtils.findAnnotation(cls, Jt808RequestMsgHandler.class);
            assert handlerAnnotation != null;

            final Method[] declaredMethods = ReflectionUtils.getAllDeclaredMethods(ClassUtils.getUserClass(cls));
            for (Method method : declaredMethods) {
                if (!isRequestMsgMappingMethod(method)) {
                    continue;
                }

                final MethodParameter[] methodParameters = getMethodParameters(method);

                final HandlerMethod handlerMethod = new HandlerMethod(cls.newInstance(), method, methodParameters);

                final Jt808RequestMsgMapping mappingAnnotation = method.getAnnotation(Jt808RequestMsgMapping.class);
                for (int msgId : mappingAnnotation.msgType()) {
                    MsgType msgType = msgTypeParser.parseMsgType(msgId)
                            .orElseThrow(() -> new JtIllegalArgumentException("Can not parse msgType with msgId " + msgId));
                    defaultHandler.addSupportedMsgType(msgType, handlerMethod);
                    msgHandlerMapping.registerHandler(msgType, defaultHandler, false);
                }
            }
        }
    }

    private MethodParameter[] getMethodParameters(Method method) {
        final int paramCount = method.getGenericParameterTypes().length;
        final MethodParameter[] methodParameters = new MethodParameter[paramCount];
        for (int i = 0; i < paramCount; i++) {
            final Type type = method.getGenericParameterTypes()[i];
            final MethodParameter parameter = new MethodParameter().setIndex(i).setGenericType(Lists.newArrayList()).setMethod(method);
            methodParameters[i] = parameter;
            if (type instanceof Class) {
                parameter.setParameterType((Class<?>) type);
            } else if (type instanceof ParameterizedType) {
                for (Type actualTypeArgument : ((ParameterizedType) type).getActualTypeArguments()) {
                    // ignore WildcardType
                    // ?, ? extends Number, or ? super Integer
                    if (actualTypeArgument instanceof WildcardType) {
                        continue;
                    }
                    parameter.setParameterType((Class<?>) ((ParameterizedType) type).getRawType());
                    parameter.getGenericType().add((Class<?>) actualTypeArgument);
                }
            } else {
                log.error("Unsupported type : {}", type);
            }
        }
        return methodParameters;
    }

    private boolean isRequestMsgMappingMethod(Method method) {
        return method.getAnnotation(Jt808RequestMsgMapping.class) != null;
    }

    private boolean isHandlerClass(Class<?> cls) {
        return AnnotationUtils.findAnnotation(cls, Jt808RequestMsgHandler.class) != null;
    }

}
