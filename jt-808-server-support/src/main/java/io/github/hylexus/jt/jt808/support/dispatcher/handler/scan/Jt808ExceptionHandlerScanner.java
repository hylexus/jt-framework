package io.github.hylexus.jt.jt808.support.dispatcher.handler.scan;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.exception.JtIllegalArgumentException;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808ExceptionHandler;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestMsgHandler;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestMsgHandlerAdvice;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver.HandlerMethodArgumentResolver;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.exception.handler.CompositeJt808ExceptionHandler;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.exception.handler.ExceptionHandlerHandlerMethod;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.github.hylexus.jt.jt808.support.utils.ReflectionUtils.isVoidReturnType;

/**
 * @author hylexus
 */
public class Jt808ExceptionHandlerScanner implements InitializingBean, ApplicationContextAware {
    @Setter
    private ApplicationContext applicationContext;

    private final CompositeJt808ExceptionHandler compositeJt808ExceptionHandler;
    private final HandlerMethodArgumentResolver argumentResolver;

    public Jt808ExceptionHandlerScanner(CompositeJt808ExceptionHandler compositeJt808ExceptionHandler, HandlerMethodArgumentResolver argumentResolver) {
        this.compositeJt808ExceptionHandler = compositeJt808ExceptionHandler;
        this.argumentResolver = argumentResolver;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        final Collection<Object> exceptionHandlerClassList = applicationContext.getBeansWithAnnotation(Jt808RequestMsgHandlerAdvice.class).values();
        int order = 1;
        for (Object handlerClassInstance : exceptionHandlerClassList) {
            ReflectionUtils.doWithMethods(
                    ClassUtils.getUserClass(handlerClassInstance.getClass()),
                    method -> {
                        final Set<Class<? extends Throwable>> supportedExceptionTypes = this.getSupportedExceptionTypes(method);
                        final Set<Jt808ProtocolVersion> supportedVersions = this.getSupportedVersions(method);
                        final ExceptionHandlerHandlerMethod handlerMethod = new ExceptionHandlerHandlerMethod(
                                handlerClassInstance, method,
                                isVoidReturnType(method),
                                supportedExceptionTypes, supportedVersions,
                                argumentResolver
                        );

                        this.compositeJt808ExceptionHandler.addExceptionHandler(handlerMethod);
                    },
                    this::isExceptionHandlerMethod
            );
        }
    }

    private Set<Jt808ProtocolVersion> getSupportedVersions(Method method) {
        final Set<Jt808ExceptionHandler> annotations = AnnotatedElementUtils.findAllMergedAnnotations(method, Jt808ExceptionHandler.class);
        return annotations.stream().flatMap(a -> Stream.of(a.versions())).collect(Collectors.toSet());
    }

    private Set<Class<? extends Throwable>> getSupportedExceptionTypes(Method method) {
        final Set<Class<? extends Throwable>> result = Sets.newHashSet();

        this.tryDetectExceptionTypeFromAnnotation(method, result);

        if (result.isEmpty()) {
            this.tryDetectExceptionTypeFromMethodArguments(method, result);
        }

        if (result.isEmpty()) {
            throw new JtIllegalArgumentException("No exception type found on method " + method);
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    private void tryDetectExceptionTypeFromMethodArguments(Method method, Set<Class<? extends Throwable>> result) {
        for (Class<?> parameterType : method.getParameterTypes()) {
            if (Throwable.class.isAssignableFrom(parameterType)) {
                result.add((Class<? extends Throwable>) parameterType);
            }
        }
    }

    private void tryDetectExceptionTypeFromAnnotation(Method method, Set<Class<? extends Throwable>> result) {
        final Set<Jt808ExceptionHandler> annotations = AnnotatedElementUtils.findAllMergedAnnotations(method, Jt808ExceptionHandler.class);
        for (Jt808ExceptionHandler annotation : annotations) {
            Class<? extends Throwable>[] exceptionClasses = annotation.value();
            result.addAll(Lists.newArrayList(exceptionClasses));
        }
    }

    private boolean isExceptionHandlerMethod(Method method) {
        return AnnotationUtils.findAnnotation(method, Jt808ExceptionHandler.class) != null;
    }

    private boolean isExceptionHandlerClass(Class<?> cls) {
        return AnnotatedElementUtils.isAnnotated(cls, Jt808RequestMsgHandler.class)
               || AnnotatedElementUtils.isAnnotated(cls, Jt808RequestMsgHandlerAdvice.class);
    }

}
