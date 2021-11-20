package io.github.hylexus.jt808.support.entity.scan;

import io.github.hylexus.jt.annotation.msg.req.Jt808ReqMsgBody;
import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt.exception.JtIllegalArgumentException;
import io.github.hylexus.jt.spring.utils.ClassScanner;
import io.github.hylexus.jt808.converter.MsgTypeParser;
import io.github.hylexus.jt808.converter.impl.CustomReflectionBasedRequestMsgBodyConverter;
import io.github.hylexus.jt808.msg.RequestMsgBody;
import io.github.hylexus.jt808.support.RequestMsgBodyConverterMapping;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.annotation.AnnotationUtils;

import java.io.IOException;
import java.util.Set;

/**
 * @author hylexus
 * Created At 2019-09-22 5:11 下午
 */
@Slf4j
public class Jt808EntityScanner implements InitializingBean {

    private final Set<String> packagesToScan;
    private final RequestMsgBodyConverterMapping msgConverterMapping;
    private final MsgTypeParser msgTypeParser;
    private final CustomReflectionBasedRequestMsgBodyConverter reflectionBasedRequestMsgBodyConverter;

    public Jt808EntityScanner(
            Set<String> packagesToScan, MsgTypeParser msgTypeParser,
            RequestMsgBodyConverterMapping msgConverterMapping, CustomReflectionBasedRequestMsgBodyConverter reflectionBasedRequestMsgBodyConverter) {

        this.packagesToScan = packagesToScan;
        this.msgTypeParser = msgTypeParser;
        this.msgConverterMapping = msgConverterMapping;
        this.reflectionBasedRequestMsgBodyConverter = reflectionBasedRequestMsgBodyConverter;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.doEntityScan(this.packagesToScan, this.reflectionBasedRequestMsgBodyConverter);
    }

    public void doEntityScan(
            Set<String> packagesToScan,
            CustomReflectionBasedRequestMsgBodyConverter reflectionBasedRequestMsgBodyConverter) throws IOException {

        if (CollectionUtils.isEmpty(packagesToScan)) {
            log.info("[jt808.entity-scan.base-packages] is empty. Skip...");
            return;
        }

        final ClassScanner scanner = new ClassScanner();
        final Set<Class> entityClass = scanner.doScan(packagesToScan, cls -> AnnotationUtils.findAnnotation(cls, Jt808ReqMsgBody.class) != null);
        if (CollectionUtils.isEmpty(entityClass)) {
            log.info("No MsgBodyEntity found for Jt808");
            return;
        }

        for (Class cls : entityClass) {
            final Jt808ReqMsgBody annotation = AnnotationUtils.findAnnotation(cls, Jt808ReqMsgBody.class);
            assert annotation != null;

            final int[] msgIds = annotation.msgType();
            for (int msgId : msgIds) {
                if (!RequestMsgBody.class.isAssignableFrom(cls)) {
                    log.error("Class {} marked by @{}, but it not a implementation of {}", cls.getSimpleName(), Jt808ReqMsgBody.class.getSimpleName(),
                            RequestMsgBody.class.getName());
                    continue;
                }
                final MsgType msgType = msgTypeParser.parseMsgType(msgId)
                        .orElseThrow(() -> new JtIllegalArgumentException("Can not parse msgType with msgId " + msgId));
                @SuppressWarnings("unchecked")
                Class<? extends RequestMsgBody> cls1 = cls;
                for (Jt808ProtocolVersion version : annotation.version()) {
                    reflectionBasedRequestMsgBodyConverter.addSupportedMsgBody(msgType, version, cls1);
                    msgConverterMapping.registerConverter(msgType, version, reflectionBasedRequestMsgBodyConverter, false);
                }
                // msgConverterMapping.registerConverter(msgType, reflectionBasedRequestMsgBodyConverter);
            }
        }
    }
}
