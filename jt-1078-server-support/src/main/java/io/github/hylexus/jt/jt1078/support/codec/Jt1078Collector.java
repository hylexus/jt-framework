package io.github.hylexus.jt.jt1078.support.codec;

import io.github.hylexus.jt.common.JtCommonUtils;
import io.github.hylexus.jt.jt1078.spec.*;
import io.github.hylexus.jt.jt1078.spec.impl.request.BuiltinJt1078MediaType;
import io.github.hylexus.jt.jt1078.spec.impl.request.DefaultJt1078PayloadType;
import io.github.hylexus.jt.jt1078.spec.impl.subpub.DefaultJt1078Subscription;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import reactor.core.publisher.FluxSink;

/**
 * @author hylexus
 */
public interface Jt1078Collector<T extends Jt1078Subscription> {

    @Getter
    class ConvertiblePair {
        private final Jt1078PayloadType sourceType;
        private final Jt1078SubscriptionType targetType;

        public ConvertiblePair(Jt1078PayloadType sourceType, Jt1078SubscriptionType targetType) {
            this.sourceType = sourceType;
            this.targetType = targetType;
        }

        public boolean support(Jt1078SubscriptionSelector selector) {
            return this.sourceType.equals(selector.sourceType())
                   && this.targetType.equals(selector.targetType());
        }
    }

    default void setSink(FluxSink<T> sink) {
    }

    ConvertiblePair supportedType();

    default boolean support(Jt1078SubscriptionSelector selector) {
        return this.supportedType().support(selector);
    }

    void collect(Jt1078Request request);

    default void close() {
    }

    class NoOpsJt1078Collector implements Jt1078Collector<Jt1078Subscription> {

        private FluxSink<Jt1078Subscription> fluxSink;

        @Override
        public ConvertiblePair supportedType() {
            return new ConvertiblePair(DefaultJt1078PayloadType.H264, BuiltinJt1078MediaType.RAW);
        }

        @Override
        public void setSink(FluxSink<Jt1078Subscription> sink) {
            this.fluxSink = sink;
        }

        @Override
        public void collect(Jt1078Request request) {
            try {
                final ByteBuf body = request.body();
                final byte[] bytes = JtCommonUtils.getBytes(body, 0, body.readableBytes());
                final Jt1078Subscription subscription = new DefaultJt1078Subscription(BuiltinJt1078MediaType.MP4, bytes);
                this.fluxSink.next(subscription);
            } catch (Throwable e) {
                this.fluxSink.error(e);
            }
            if (someConditionMatched()) {
                this.fluxSink.complete();
            }
        }

        private boolean someConditionMatched() {
            return false;
        }
    }
}
