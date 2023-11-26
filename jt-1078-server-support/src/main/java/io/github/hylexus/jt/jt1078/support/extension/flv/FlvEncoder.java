package io.github.hylexus.jt.jt1078.support.extension.flv;

import io.github.hylexus.jt.jt1078.spec.Jt1078Request;
import io.netty.buffer.ByteBuf;

import java.util.List;

public interface FlvEncoder {

    List<ByteBuf> encode(Jt1078Request request);

    default void close() {
    }
}
