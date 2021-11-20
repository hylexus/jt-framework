package io.github.hylexus.jt808.handler;

import io.github.hylexus.jt808.msg.RequestMsgBody;

public abstract class AbstractBuiltinRequestMsgHandler<T extends RequestMsgBody> extends AbstractMsgHandler<T> {

    @Override
    public int getOrder() {
        return BUILTIN_COMPONENT_ORDER;
    }

}
