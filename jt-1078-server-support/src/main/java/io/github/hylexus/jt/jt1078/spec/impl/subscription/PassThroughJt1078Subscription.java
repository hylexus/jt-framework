package io.github.hylexus.jt.jt1078.spec.impl.subscription;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt1078.spec.Jt1078Request;
import io.github.hylexus.jt.jt1078.spec.Jt1078RequestHeader;
import io.github.hylexus.jt.utils.ByteBufUtils;
import io.netty.buffer.ByteBuf;

/**
 * 用来调试的默认实现。
 * <p>
 * 该实现没有对音视频做任何转换(原样输出终端上报的数据流的 {@link Jt1078Request#rawByteBuf()}  部分)。
 *
 * @see Jt1078Request#rawByteBuf()
 */
@BuiltinComponent
public class PassThroughJt1078Subscription extends ByteArrayJt1078Subscription {
    private static final byte[] EMPTY = new byte[0];
    private final Jt1078RequestHeader header;

    public PassThroughJt1078Subscription(Jt1078Request request) {
        super(DefaultJt1078SubscriptionType.PASS_THROUGH_RAW_DATA, getBytes(request));
        this.header = request.header();
    }

    public Jt1078RequestHeader header() {
        return header;
    }

    private static byte[] getBytes(Jt1078Request request) {
        final ByteBuf byteBuf = request.rawByteBuf();
        if (byteBuf == null) {
            return EMPTY;
        }
        return ByteBufUtils.getBytes(byteBuf);
    }

}
