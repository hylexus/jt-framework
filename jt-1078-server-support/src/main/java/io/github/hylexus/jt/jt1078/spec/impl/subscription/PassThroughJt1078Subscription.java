package io.github.hylexus.jt.jt1078.spec.impl.subscription;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt1078.spec.Jt1078Request;
import io.github.hylexus.jt.jt1078.spec.Jt1078RequestHeader;
import io.github.hylexus.jt.jt1078.spec.Jt1078Subscription;
import io.github.hylexus.jt.utils.ByteBufUtils;

/**
 * 用来调试的默认实现。
 * <p>
 * 该实现没有对音视频做任何转换(原样输出终端上报的数据流的 {@link Jt1078Request#rawByteBuf()}  部分)。
 *
 * @see Jt1078Request#rawByteBuf()
 */
@BuiltinComponent
public class PassThroughJt1078Subscription implements Jt1078Subscription {
    private final Jt1078RequestHeader header;
    private final byte[] data;

    public PassThroughJt1078Subscription(Jt1078Request request) {
        this.header = request.header();
        this.data = ByteBufUtils.getBytes(request.rawByteBuf(), 0, request.rawByteBuf().readableBytes());
    }

    public byte[] getData() {
        return data;
    }

    public Jt1078RequestHeader getHeader() {
        return header;
    }
}
