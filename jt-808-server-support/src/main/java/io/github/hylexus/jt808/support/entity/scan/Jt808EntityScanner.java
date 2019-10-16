package io.github.hylexus.jt808.support.entity.scan;

import io.github.hylexus.jt.annotation.msg.req.Jt808ReqMsgBody;
import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt.exception.JtIllegalArgumentException;
import io.github.hylexus.jt.spring.utils.ClassScanner;
import io.github.hylexus.jt808.converter.MsgTypeParser;
import io.github.hylexus.jt808.converter.impl.ReflectionBasedRequestMsgBodyConverter;
import io.github.hylexus.jt808.msg.RequestMsgBody;
import io.github.hylexus.jt808.support.MsgConverterMapping;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.Set;

/**
 * @author hylexus
 * Created At 2019-09-22 5:11 下午
 */
@Slf4j
public class Jt808EntityScanner implements InitializingBean {

    private Set<String> packagesToScan;
    private MsgConverterMapping msgConverterMapping;
    private MsgTypeParser msgTypeParser;

    public Jt808EntityScanner(Set<String> packagesToScan, MsgTypeParser msgTypeParser, MsgConverterMapping msgConverterMapping) {
        this.packagesToScan = packagesToScan;
        this.msgTypeParser = msgTypeParser;
        this.msgConverterMapping = msgConverterMapping;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (CollectionUtils.isEmpty(packagesToScan)) {
            log.info("[jt808.packages-to-entity-scan] is empty. Skip...");
            return;
        }

        ClassScanner scanner = new ClassScanner();
        Set<Class> entityClass = scanner.doScan(packagesToScan, cls -> AnnotationUtils.findAnnotation(cls, Jt808ReqMsgBody.class) != null);
        if (CollectionUtils.isEmpty(entityClass)) {
            log.info("No MsgBodyEntity found for Jt808");
            return;
        }

        ReflectionBasedRequestMsgBodyConverter defaultConverter = new ReflectionBasedRequestMsgBodyConverter();
        for (Class cls : entityClass) {
            Jt808ReqMsgBody annotation = AnnotationUtils.findAnnotation(cls, Jt808ReqMsgBody.class);
            assert annotation != null;

            int[] msgIds = annotation.msgType();
            for (int msgId : msgIds) {
                if (!RequestMsgBody.class.isAssignableFrom(cls)) {
                    log.error("Class {} marked by @Jt808MsgBody, but it not a implementation of {}", cls.getSimpleName(),
                            RequestMsgBody.class.getName());
                    continue;
                }
                MsgType msgType = msgTypeParser.parseMsgType(msgId)
                        .orElseThrow(() -> new JtIllegalArgumentException("Can not parse msgType with msgId " + msgId));
                @SuppressWarnings("unchecked")
                Class<? extends RequestMsgBody> cls1 = cls;
                defaultConverter.addSupportedMsgBody(msgType, cls1);
                msgConverterMapping.registerConverter(msgType, defaultConverter);
            }
        }
    }
}
