package io.github.hylexus.jt808.converter.impl;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.msg.req.BuiltinRawBytesRequestMsgBody;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * Created At 2020-12-05 13:54
 *
 * @author hylexus
 */
@Slf4j
@BuiltinComponent
public class BuiltinRawBytesRequestMsgBodyConverter extends AbstractBuiltinRequestMsgBodyConverter<BuiltinRawBytesRequestMsgBody> {

    @Override
    public Optional<BuiltinRawBytesRequestMsgBody> convert2Entity(RequestMsgMetadata metadata) {
        if (log.isDebugEnabled()) {
            log.debug("[RawBytesWrapper] returned for msgType:{}", metadata.getMsgType());
        }
        return Optional.of(new BuiltinRawBytesRequestMsgBody(metadata.getBodyBytes()));
    }
}
