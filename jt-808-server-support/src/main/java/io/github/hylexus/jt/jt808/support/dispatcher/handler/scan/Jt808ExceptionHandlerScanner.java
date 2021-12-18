package io.github.hylexus.jt.jt808.support.dispatcher.handler.scan;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.github.hylexus.jt.core.BuiltinComponent;
import io.github.hylexus.jt.core.OrderedComponent;
import io.github.hylexus.jt.exception.JtIllegalArgumentException;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808ExceptionHandler;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestHandler;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestHandlerAdvice;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.argument.resolver.Jt808HandlerMethodArgumentResolver;
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

import static io.github.hylexus.jt.jt808.support.utils.ReflectionUtils.isVoidReturnType;

/**
 * @author hylexus
 */
public class Jt808ExceptionHandlerScanner implements InitializingBean, ApplicationContextAware {
    @Setter
    private ApplicationContext applicationContext;

    private final CompositeJt808ExceptionHandler compositeJt808ExceptionHandler;
    private final Jt808HandlerMethodArgumentResolver argumentResolver;

    public Jt808ExceptionHandlerScanner(CompositeJt808ExceptionHandler compositeJt808ExceptionHandler, Jt808HandlerMethodArgumentResolver argumentResolver) {
        this.compositeJt808ExceptionHandler = compositeJt808ExceptionHandler;
        this.argumentResolver = argumentResolver;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        final Collection<Object> exceptionHandlerClassList = applicationContext.getBeansWithAnnotation(Jt808RequestHandlerAdvice.class).values();
        this.doScan(exceptionHandlerClassList);

        final Collection<Object> msgHandlerClassList = applicationContext.getBeansWithAnnotation(Jt808RequestHandler.class).values();
        this.doScan(msgHandlerClassList);

        this.compositeJt808ExceptionHandler.sort();
    }

    private void doScan(Collection<Object> exceptionHandlerClassList) {
        for (Object handlerClassInstance : exceptionHandlerClassList) {
            final int order;
            if (handlerClassInstance instanceof OrderedComponent) {
                order = ((OrderedComponent) handlerClassInstance).getOrder();
            } else if (handlerClassInstance instanceof BuiltinComponent) {
                order = OrderedComponent.LOWEST_PRECEDENCE;
            } else {
                order = OrderedComponent.DEFAULT_ORDER;
            }
            ReflectionUtils.doWithMethods(
                    ClassUtils.getUserClass(handlerClassInstance.getClass()),
                    method -> {
                        final Set<Class<? extends Throwable>> supportedExceptionTypes = this.getSupportedExceptionTypes(method);
                        final ExceptionHandlerHandlerMethod handlerMethod = new ExceptionHandlerHandlerMethod(
                                handlerClassInstance, method,
                                isVoidReturnType(method),
                                supportedExceptionTypes,
                                argumentResolver, order
                        );

                        this.compositeJt808ExceptionHandler.addExceptionHandler(handlerMethod);
                    },
                    this::isExceptionHandlerMethod
            );
        }
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
        return AnnotatedElementUtils.isAnnotated(cls, Jt808RequestHandler.class)
               || AnnotatedElementUtils.isAnnotated(cls, Jt808RequestHandlerAdvice.class);
    }

}
