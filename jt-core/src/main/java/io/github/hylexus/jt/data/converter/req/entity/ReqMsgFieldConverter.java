package io.github.hylexus.jt.data.converter.req.entity;

/**
 * @author hylexus
 * Created At 2019-09-18 10:02 下午
 */
@FunctionalInterface
public interface ReqMsgFieldConverter<T> {

    T convert(byte[] bytes, byte[] subSeq);

    class NoOpsConverter implements ReqMsgFieldConverter<Object> {
        @Override
        public Object convert(byte[] bytes, byte[] subSeq) {
            return null;
        }
    }

}
