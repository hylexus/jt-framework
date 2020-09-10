package io.github.hylexus.jt808.support.handler.scan;

import io.github.hylexus.jt.annotation.msg.handler.Jt808RequestMsgHandler;
import io.github.hylexus.jt.annotation.msg.handler.Jt808RequestMsgHandlerMapping;
import io.github.hylexus.jt.annotation.msg.resp.Jt808RespMsgBody;
import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt.exception.JtIllegalArgumentException;
import io.github.hylexus.jt.spring.utils.ClassScanner;
import io.github.hylexus.jt808.converter.MsgTypeParser;
import io.github.hylexus.jt808.handler.AbstractMsgHandler;
import io.github.hylexus.jt808.handler.impl.reflection.CustomReflectionBasedRequestMsgHandler;
import io.github.hylexus.jt808.handler.impl.reflection.HandlerMethod;
import io.github.hylexus.jt808.msg.RespMsgBody;
import io.github.hylexus.jt808.support.MsgHandlerMapping;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static io.github.hylexus.jt.utils.ReflectionUtils.isVoidReturnType;

/**
 * @author hylexus
 * Created At 2020-02-01 3:31 下午
 */
@Slf4j(topic = "jt-808.handler-scan")
public class Jt808MsgHandlerScanner implements InitializingBean, ApplicationContextAware {

    private final Set<String> packagesToScan;
    private final MsgTypeParser msgTypeParser;
    private final MsgHandlerMapping msgHandlerMapping;
    private final CustomReflectionBasedRequestMsgHandler reflectionBasedRequestMsgHandler;
    private ApplicationContext applicationContext;

    public Jt808MsgHandlerScanner(
            Set<String> packagesToScan, MsgTypeParser msgTypeParser,
            MsgHandlerMapping msgHandlerMapping, CustomReflectionBasedRequestMsgHandler reflectionBasedRequestMsgHandler) {

        this.packagesToScan = packagesToScan;
        this.msgTypeParser = msgTypeParser;
        this.msgHandlerMapping = msgHandlerMapping;
        this.reflectionBasedRequestMsgHandler = reflectionBasedRequestMsgHandler;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 通过普通注解扫描 [MsgHandler], 不会加入Spring容器
        detectMsgHandlerByAnnotation();

        // 从Spring容器中获取 [MsgHandler], 针对于受Spring管理的 [MsgHandler]
        detectMsgHandlerFromSpringContainer();
    }

    private void detectMsgHandlerByAnnotation() throws IOException, InstantiationException, IllegalAccessException {
        if (CollectionUtils.isEmpty(packagesToScan)) {
            log.info("[jt808.handler-scan.base-packages] is empty. Skip...");
            return;
        }

        final ClassScanner scanner = new ClassScanner();
        @SuppressWarnings("rawtypes") final Set<Class> handlerClassList = scanner.doScan(packagesToScan, this::isHandlerClass);
        if (CollectionUtils.isNotEmpty(handlerClassList)) {
            detectMsgHandler(reflectionBasedRequestMsgHandler, handlerClassList);
        }
    }

    private void detectMsgHandlerFromSpringContainer() throws IOException, InstantiationException, IllegalAccessException {
        @SuppressWarnings("rawtypes") final Set<Class> handlerClassListFromSpringContainer = applicationContext
                .getBeansWithAnnotation(Jt808RequestMsgHandler.class)
                .values().stream().map(Object::getClass).collect(Collectors.toSet());
        if (!handlerClassListFromSpringContainer.isEmpty()) {
            detectMsgHandler(reflectionBasedRequestMsgHandler, handlerClassListFromSpringContainer);
        }
    }

    public void detectMsgHandler(CustomReflectionBasedRequestMsgHandler handler, Set<Class> handlerClassList) throws IOException, InstantiationException,
            IllegalAccessException {

        //final ReflectionBasedRequestMsgHandler defaultHandler = new ReflectionBasedRequestMsgHandler(argumentResolver, responseMsgBodyConverter);
        for (Class<?> cls : handlerClassList) {
            final Jt808RequestMsgHandler handlerAnnotation = AnnotationUtils.findAnnotation(cls, Jt808RequestMsgHandler.class);
            assert handlerAnnotation != null;

            if (AbstractMsgHandler.class.isAssignableFrom(cls)) {
                Optional<MsgType> optionalMsgType = msgTypeParser.parseMsgType(handlerAnnotation.msgType());
                if (optionalMsgType.isPresent()) {
                    msgHandlerMapping.registerHandler(optionalMsgType.get(), (AbstractMsgHandler) createBeanInstance(cls));
                    continue;
                }
            }

            final Method[] declaredMethods = ReflectionUtils.getAllDeclaredMethods(ClassUtils.getUserClass(cls));
            for (Method method : declaredMethods) {

                if (!isRequestMsgMappingMethod(method)) {
                    continue;
                }

                if (!isSupportedReturnType(method)) {
                    continue;
                }

                final HandlerMethod handlerMethod = new HandlerMethod(createBeanInstance(cls), method, isVoidReturnType(method));
                final Jt808RequestMsgHandlerMapping mappingAnnotation = method.getAnnotation(Jt808RequestMsgHandlerMapping.class);

                for (int msgId : mappingAnnotation.msgType()) {
                    MsgType msgType = msgTypeParser.parseMsgType(msgId)
                            .orElseThrow(() -> new JtIllegalArgumentException("Can not parse msgType with msgId " + msgId));
                    handler.addSupportedMsgType(msgType, handlerMethod);
                }
            }
            msgHandlerMapping.registerHandler(handler, false);
        }
    }

    private boolean isSupportedReturnType(Method method) {
        if (RespMsgBody.class.isAssignableFrom(method.getReturnType())) {
            return true;
        }

        if (AnnotationUtils.findAnnotation(method.getReturnType(), Jt808RespMsgBody.class) != null) {
            return true;
        }

        if (isVoidReturnType(method)) {
            log.info("HandlerMethod [{}] returns void, which means no data will be returned to the client.", method);
            return true;
        }

        log.error("Method [{}] returned an unsupported type : [{}], only [{}] is supported by {}",
                method, method.getReturnType(), RespMsgBody.class, Jt808RequestMsgHandler.class);
        return false;
    }

    private Object createBeanInstance(Class<?> cls) throws InstantiationException, IllegalAccessException {
        String[] names = applicationContext.getBeanNamesForType(cls);
        if (names.length != 0) {
            return applicationContext.getBean(cls);
        }
        return cls.newInstance();
    }


    private boolean isRequestMsgMappingMethod(Method method) {
        return method.getAnnotation(Jt808RequestMsgHandlerMapping.class) != null;
    }

    private boolean isHandlerClass(Class<?> cls) {
        return AnnotationUtils.findAnnotation(cls, Jt808RequestMsgHandler.class) != null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
