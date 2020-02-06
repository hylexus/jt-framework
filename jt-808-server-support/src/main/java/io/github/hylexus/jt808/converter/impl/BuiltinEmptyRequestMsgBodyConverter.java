package io.github.hylexus.jt808.converter.impl;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.data.msg.BuiltinJt808MsgType;
import io.github.hylexus.jt808.converter.RequestMsgBodyConverter;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.msg.req.BuiltinEmptyRequestMsgBody;

import java.util.Optional;

/**
 * @author hylexus
 * createdAt 2019/2/5
 * @see BuiltinJt808MsgType#CLIENT_HEART_BEAT
 **/
@BuiltinComponent
public class BuiltinEmptyRequestMsgBodyConverter implements RequestMsgBodyConverter<BuiltinEmptyRequestMsgBody> {

    @Override
    public Optional<BuiltinEmptyRequestMsgBody> convert2Entity(RequestMsgMetadata metadata) {
        return Optional.of(new BuiltinEmptyRequestMsgBody());
    }
}
