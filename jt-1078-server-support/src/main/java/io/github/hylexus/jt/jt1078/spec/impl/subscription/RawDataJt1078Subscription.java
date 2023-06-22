package io.github.hylexus.jt.jt1078.spec.impl.subscription;

import io.github.hylexus.jt.jt1078.spec.Jt1078Request;
import io.github.hylexus.jt.jt1078.spec.Jt1078RequestHeader;
import io.github.hylexus.jt.utils.ByteBufUtils;

/**
 * 用来调试的默认实现。
 * <p>
 * 该实现没有对音视频做任何转换(原样输出终端上报的数据流的 {@link Jt1078Request#body()} 部分)。
 *
 * @see Jt1078Request#body()
 */
public class RawDataJt1078Subscription extends ByteArrayJt1078Subscription {
    private final Jt1078RequestHeader header;

    public RawDataJt1078Subscription(Jt1078Request request) {
        super(DefaultJt1078SubscriptionType.RAW_DATA, ByteBufUtils.getBytes(request.body()));
        this.header = request.header();
    }

    public Jt1078RequestHeader header() {
        return header;
    }

}
