package io.github.hylexus.jt.data.resp;

/**
 * @author hylexus
 * Created At 2019-10-17 10:46 下午
 */
public interface BytesValueWrapper<T> {

    byte[] getAsBytes();

    T getValue();
}
