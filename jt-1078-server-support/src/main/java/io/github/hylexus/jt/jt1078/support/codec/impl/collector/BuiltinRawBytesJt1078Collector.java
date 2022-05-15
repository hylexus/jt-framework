package io.github.hylexus.jt.jt1078.support.codec.impl.collector;

import io.github.hylexus.jt.common.JtCommonUtils;
import io.github.hylexus.jt.jt1078.spec.Jt1078PayloadType;
import io.github.hylexus.jt.jt1078.spec.Jt1078Request;
import io.github.hylexus.jt.jt1078.spec.Jt1078Subscription;
import io.github.hylexus.jt.jt1078.spec.Jt1078SubscriptionSelector;
import io.github.hylexus.jt.jt1078.spec.impl.request.DefaultJt1078SubscriptionType;
import io.github.hylexus.jt.jt1078.spec.impl.subscription.DefaultJt1078Subscription;
import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hylexus
 */
@Slf4j
public class BuiltinRawBytesJt1078Collector extends AbstractJt1078Collector {

    public BuiltinRawBytesJt1078Collector() {
    }

    @Override
    public boolean support(Jt1078Request request) {
        return true;
    }

    @Override
    public boolean support(Jt1078SubscriptionSelector selector) {
        return selector.targetType() == DefaultJt1078SubscriptionType.RAW;
    }

    @Override
    public void collect(Jt1078Request request) {
        final Jt1078PayloadType payloadType = request.header().payloadType();
        log.info(">>> pt={}", payloadType);

        try {
            this.doProcess(request);
        } catch (Throwable e) {
            this.forEachSink(sink -> sink.error(e));
        }
    }

    private void doProcess(Jt1078Request request) {
        final ByteBuf body = request.body();
        final byte[] bytes = JtCommonUtils.getBytes(body, 0, body.readableBytes());
        final Jt1078Subscription subscription = new DefaultJt1078Subscription(DefaultJt1078SubscriptionType.RAW, bytes);

        this.forEachSink(sink -> sink.next(subscription));
    }

}
