package io.github.hylexus.jt.jt808.support.data.converter;

import io.netty.buffer.ByteBuf;

/**
 * @author hylexus
 */
@FunctionalInterface
public interface ReqMsgFieldConverter<T> {

    T convert(ByteBuf byteBuf, int start, int length);

    class NoOpsConverter implements ReqMsgFieldConverter<Object> {
        @Override
        public Object convert(ByteBuf byteBuf,int start,int length) {
            return null;
        }
    }

}
