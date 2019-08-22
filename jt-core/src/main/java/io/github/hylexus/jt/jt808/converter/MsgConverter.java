package io.github.hylexus.jt.jt808.converter;

import io.github.hylexus.jt.jt808.msg.AbstractRequestMsg;

import java.util.Optional;

/**
 * @author hylexus
 * createdAt 2018/12/28
 **/
public interface MsgConverter<T extends AbstractRequestMsg> {

    Optional<T> convert2SubMsg(AbstractRequestMsg abstractMsg);

}
