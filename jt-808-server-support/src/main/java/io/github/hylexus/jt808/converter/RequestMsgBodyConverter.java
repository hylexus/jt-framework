package io.github.hylexus.jt808.converter;

import io.github.hylexus.jt808.msg.RequestMsgBody;
import io.github.hylexus.jt808.msg.RequestMsgWrapper;

import java.util.Optional;

/**
 * @author hylexus
 * createdAt 2018/12/28
 **/
public interface RequestMsgBodyConverter<E extends RequestMsgBody> {

    Optional<E> convert2Entity(RequestMsgWrapper wrapper);

}
