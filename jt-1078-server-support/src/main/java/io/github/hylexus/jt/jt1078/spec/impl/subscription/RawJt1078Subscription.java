package io.github.hylexus.jt.jt1078.spec.impl.subscription;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt1078.spec.Jt1078Request;
import io.github.hylexus.jt.jt1078.spec.Jt1078RequestHeader;
import io.github.hylexus.jt.jt1078.spec.Jt1078Subscription;
import io.github.hylexus.jt.utils.ByteBufUtils;

/**
 * 用来调试的默认实现。
 * <p>
 * 该实现没有对音视频做任何转换(原样输出终端上报的数据流的 {@link Jt1078Request#body()} 部分)。
 *
 * @see Jt1078Request#body()
 */
@BuiltinComponent
public class RawJt1078Subscription implements Jt1078Subscription {
    private final Jt1078RequestHeader header;
    private final byte[] data;

    public RawJt1078Subscription(Jt1078Request request) {
        this.header = request.header();
        this.data = ByteBufUtils.getBytes(request.body(), 0, header.msgBodyLength());
    }

    public Jt1078RequestHeader getHeader() {
        return header;
    }

    public byte[] getData() {
        return data;
    }
}
