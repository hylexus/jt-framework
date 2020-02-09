package io.github.hylexus.jt808.support.exception.scan;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.github.hylexus.jt.annotation.msg.handler.Jt808ExceptionHandler;
import io.github.hylexus.jt.annotation.msg.handler.Jt808RequestMsgHandler;
import io.github.hylexus.jt.annotation.msg.handler.Jt808RequestMsgHandlerAdvice;
import io.github.hylexus.jt.exception.JtIllegalArgumentException;
import io.github.hylexus.jt.spring.utils.ClassScanner;
import io.github.hylexus.jt808.handler.impl.exception.DelegateExceptionHandler;
import io.github.hylexus.jt808.handler.impl.exception.ExceptionHandlerMethodExceptionHandler;
import io.github.hylexus.jt808.handler.impl.reflection.argument.resolver.HandlerMethodArgumentResolver;
import io.github.hylexus.jt808.support.OrderedComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Set;

import static io.github.hylexus.jt.utils.ReflectionUtils.isVoidReturnType;

/**
 * @author hylexus
 * Created At 2020-02-08 3:23 下午
 */
@Slf4j
public class Jt808ExceptionHandlerScanner implements InitializingBean {

    private final Set<String> packagesToScan;
    private final DelegateExceptionHandler exceptionHandler;
    private final HandlerMethodArgumentResolver argumentResolver;

    public Jt808ExceptionHandlerScanner(Set<String> packagesToScan, DelegateExceptionHandler exceptionHandler, HandlerMethodArgumentResolver argumentResolver) {
        this.packagesToScan = packagesToScan;
        this.exceptionHandler = exceptionHandler;
        this.argumentResolver = argumentResolver;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.doScan(this.packagesToScan, OrderedComponent.DEFAULT_ORDER);
    }

    public void doScan(Set<String> packagesToScan, int order) throws IOException, InstantiationException, IllegalAccessException {
        if (CollectionUtils.isEmpty(packagesToScan)) {
            log.info("[jt808.exception-handler-scan.base-packages] is empty. Skip...");
            return;
        }

        final ClassScanner scanner = new ClassScanner();
        //noinspection rawtypes
        final Set<Class> classes = scanner.doScan(packagesToScan, this::isExceptionHandlerClass);

        for (Class<?> cls : classes) {
            final Method[] declaredMethods = ReflectionUtils.getAllDeclaredMethods(ClassUtils.getUserClass(cls));
            final Object beanInstance = createBeanInstance(cls);
            for (Method method : declaredMethods) {
                if (!isExceptionHandlerMethod(method)) {
                    continue;
                }

                final Set<Class<? extends Throwable>> supportedExceptionTypes = this.getSupportedExceptionTypes(method);

                final ExceptionHandlerMethod handlerMethod =
                        new ExceptionHandlerMethod(beanInstance, method, isVoidReturnType(method), supportedExceptionTypes);

                this.exceptionHandler.addExceptionHandler(new ExceptionHandlerMethodExceptionHandler(handlerMethod, argumentResolver, order));
            }
        }
    }

    private Object createBeanInstance(Class<?> cls) throws IllegalAccessException, InstantiationException {
        return cls.newInstance();
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
