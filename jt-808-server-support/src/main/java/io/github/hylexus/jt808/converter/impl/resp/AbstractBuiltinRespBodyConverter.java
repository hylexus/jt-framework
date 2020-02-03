package io.github.hylexus.jt808.converter.impl.resp;

import io.github.hylexus.jt808.converter.ResponseMsgBodyConverter;

/**
 * @author hylexus
 * Created At 2020-02-02 4:08 下午
 */
public abstract class AbstractBuiltinRespBodyConverter implements ResponseMsgBodyConverter {

    @Override
    public int getOrder() {
        return ANNOTATION_BASED_DEV_COMPONENT_ORDER;
    }

}
