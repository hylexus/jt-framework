package io.github.hylexus.jt808.support.converter.scan;

import io.github.hylexus.jt.annotation.msg.handler.Jt808RequestMsgConverter;
import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt.spring.utils.ClassScanner;
import io.github.hylexus.jt808.converter.MsgTypeParser;
import io.github.hylexus.jt808.converter.RequestMsgBodyConverter;
import io.github.hylexus.jt808.support.RequestMsgBodyConverterMapping;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lirenhao
 * date: 2020/9/10 4:34 下午
 */
@Slf4j(topic = "jt-808.converter-scan")
public class Jt808MsgConverterScanner implements InitializingBean, ApplicationContextAware {

    private final Set<String> packagesToScan;
    private final MsgTypeParser msgTypeParser;
    private final RequestMsgBodyConverterMapping requestMsgBodyConverterMapping;
    private ApplicationContext applicationContext;

    public Jt808MsgConverterScanner(Set<String> packagesToScan, MsgTypeParser msgTypeParser,
                                    RequestMsgBodyConverterMapping requestMsgBodyConverterMapping) {
        this.packagesToScan = packagesToScan;
        this.msgTypeParser = msgTypeParser;
        this.requestMsgBodyConverterMapping = requestMsgBodyConverterMapping;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 通过普通注解扫描 [Jt808RequestMsgConverter], 不会加入Spring容器
        detectMsgConverterByAnnotation();

        // 从Spring容器中获取 [Jt808RequestMsgConverter], 针对于受Spring管理的 [Jt808RequestMsgConverter]
        detectMsgConverterFromSpringContainer();
    }

    private void detectMsgConverterByAnnotation() throws IOException, InstantiationException, IllegalAccessException {
        if (CollectionUtils.isEmpty(packagesToScan)) {
            log.info("[jt808.converter-scan.base-packages] is empty. Skip...");
            return;
        }

        final ClassScanner scanner = new ClassScanner();
        @SuppressWarnings("rawtypes") final Set<Class> converterClassList = scanner.doScan(packagesToScan, this::isConverterClass);
        if (CollectionUtils.isNotEmpty(converterClassList)) {
            detectMsgHandler(converterClassList);
        }
    }

    private void detectMsgConverterFromSpringContainer() throws InstantiationException, IllegalAccessException {
        @SuppressWarnings("rawtypes") final Set<Class> converterClassListFromSpringContainer = applicationContext
                .getBeansWithAnnotation(Jt808RequestMsgConverter.class)
                .values().stream().map(Object::getClass).collect(Collectors.toSet());
        if (!converterClassListFromSpringContainer.isEmpty()) {
            detectMsgHandler(converterClassListFromSpringContainer);
        }
    }

    public void detectMsgHandler(Set<Class> handlerClassList) throws InstantiationException,
            IllegalAccessException {
        for (Class<?> cls : handlerClassList) {
            final Jt808RequestMsgConverter handlerAnnotation = AnnotationUtils.findAnnotation(cls, Jt808RequestMsgConverter.class);
            assert handlerAnnotation != null;

            if (RequestMsgBodyConverter.class.isAssignableFrom(cls)) {
                Optional<MsgType> optionalMsgType = msgTypeParser.parseMsgType(handlerAnnotation.msgType());
                if (optionalMsgType.isPresent()) {
                    requestMsgBodyConverterMapping.registerConverter(optionalMsgType.get(), (RequestMsgBodyConverter) createBeanInstance(cls));
                }
            }
        }
    }

    private Object createBeanInstance(Class<?> cls) throws InstantiationException, IllegalAccessException {
        String[] names = applicationContext.getBeanNamesForType(cls);
        if (names.length != 0) {
            return applicationContext.getBean(cls);
        }
        return cls.newInstance();
    }

    private boolean isConverterClass(Class<?> cls) {
        return AnnotationUtils.findAnnotation(cls, Jt808RequestMsgConverter.class) != null
                && RequestMsgBodyConverter.class.isAssignableFrom(cls);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
