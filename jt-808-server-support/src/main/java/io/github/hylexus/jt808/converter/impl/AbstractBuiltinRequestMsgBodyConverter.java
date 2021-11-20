package io.github.hylexus.jt808.converter.impl;

import io.github.hylexus.jt808.converter.RequestMsgBodyConverter;
import io.github.hylexus.jt808.msg.RequestMsgBody;

/**
 * Created At 2020-02-02 4:31 下午
 *
 * @author hylexus
 */
public abstract class AbstractBuiltinRequestMsgBodyConverter<E extends RequestMsgBody> implements RequestMsgBodyConverter<E> {

    @Override
    public int getOrder() {
        return BUILTIN_COMPONENT_ORDER;
    }
}
