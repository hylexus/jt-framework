package io.github.hylexus.jt808.converter.impl;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hylexus
 * Created At 2019-09-19 10:35 下午
 */
@Slf4j
@BuiltinComponent
public class BuiltinCustomReflectionBasedRequestMsgBodyConverter extends CustomReflectionBasedRequestMsgBodyConverter {

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }

}
