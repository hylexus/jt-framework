package io.github.hylexus.jt.jt1078.support.codec.impl.collector;

import io.github.hylexus.jt.jt1078.spec.Jt1078PayloadType;
import io.github.hylexus.jt.jt1078.spec.Jt1078Request;
import io.github.hylexus.jt.jt1078.spec.Jt1078SubscriberCreator;
import io.github.hylexus.jt.jt1078.spec.Jt1078Subscription;
import io.github.hylexus.jt.jt1078.spec.impl.request.DefaultJt1078PayloadType;
import io.github.hylexus.jt.jt1078.spec.impl.subscription.ByteArrayJt1078Subscription;
import io.github.hylexus.jt.jt1078.spec.impl.subscription.DefaultJt1078SubscriptionType;
import io.github.hylexus.jt.jt1078.support.extension.flv.FlvHeader;
import io.github.hylexus.jt.jt1078.support.extension.flv.impl.DefaultFlvEncoder;
import io.github.hylexus.jt.utils.ByteBufUtils;
import io.github.hylexus.jt.utils.Jdk8Adapter;
import io.netty.buffer.ByteBuf;
import reactor.core.publisher.FluxSink;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadFactory;

/**
 * H.264 --> FLV
 */
public class H264ToFlvJt1078ChannelCollector
        extends AbstractAsyncChannelCollector<ByteArrayJt1078Subscription, H264ToFlvSubscriber> {

    static final Set<Jt1078PayloadType> SUPPORTED_PAYLOAD_TYPES = Jdk8Adapter.setOf(
            DefaultJt1078PayloadType.H264,
            DefaultJt1078PayloadType.ADPCMA,
            DefaultJt1078PayloadType.G_726,
            DefaultJt1078PayloadType.G_711A
    );
    private final DefaultFlvEncoder flvEncoder;
    private static final Map<Jt1078PayloadType, Boolean> WARNING_FLAGS = new HashMap<>();

    public H264ToFlvJt1078ChannelCollector(ThreadFactory threadFactory, Jt1078SubscriberCreator creator) {
        super(threadFactory);
        this.flvEncoder = new DefaultFlvEncoder(creator);
    }

    private boolean isSupported(Jt1078PayloadType payloadType) {
        return SUPPORTED_PAYLOAD_TYPES.contains(payloadType);
    }

    @Override
    protected void doCollect(Jt1078Request request) {
        final Jt1078PayloadType payloadType = request.header().payloadType();
        if (!this.isSupported(payloadType)) {
            this.doErrorLogIfNecessary(request, payloadType);
            return;
        }
        final List<ByteBuf> bufList = flvEncoder.encode(request);
        // 一般来说: bufList 只有一个元素 ==> 外层循环次数只有一次
        for (final ByteBuf byteBuf : bufList) {
            try {
                final ByteArrayJt1078Subscription subscription = Jt1078Subscription.forByteArray(DefaultJt1078SubscriptionType.FLV, ByteBufUtils.getBytes(byteBuf));
                // 内层循环的次数取决于订阅者的数量(同一个 SIM 同一个 channel)
                for (final H264ToFlvSubscriber subscriber : this.subscriberMapping.values()) {
                    final FluxSink<ByteArrayJt1078Subscription> sink = subscriber.sink();

                    // 1. flv-header + sps + pps
                    if (!subscriber.isFlvHeaderSent()) {
                        final ByteBuf basicFrame = flvEncoder.getFlvBasicFrame();
                        if (basicFrame != null) {
                            // header
                            sink.next(Jt1078Subscription.forByteArray(DefaultJt1078SubscriptionType.FLV, FlvHeader.of(true, this.flvEncoder.isHasAudio()).toBytes(true)));
                            // sps+pps
                            sink.next(Jt1078Subscription.forByteArray(DefaultJt1078SubscriptionType.FLV, basicFrame));
                            subscriber.setFlvHeaderSent(true);
                        }
                    }

                    // 2. lastIFrame
                    if (!subscriber.isLastIFrameSent()) {
                        final ByteBuf lastIFrame = flvEncoder.getLastIFrame();
                        if (lastIFrame != null) {
                            sink.next(Jt1078Subscription.forByteArray(DefaultJt1078SubscriptionType.FLV, lastIFrame));
                            subscriber.setLastIFrameSent(true);
                        }
                    }

                    // 确保先把 关键帧 发送出去(中途订阅)
                    if (!subscriber.isLastIFrameSent()) {
                        return;
                    }
                    // 确保先把 flvHeader 发送出去(中途订阅)
                    if (!subscriber.isFlvHeaderSent()) {
                        return;
                    }
                    // 3. data
                    sink.next(subscription);
                }
            } finally {
                byteBuf.release();
            }
        }
    }


    @Override
    protected H264ToFlvSubscriber createSubscribe(String uuid, Jt1078SubscriberCreator creator, FluxSink<ByteArrayJt1078Subscription> fluxSink) {
        return new H264ToFlvSubscriber(uuid, creator.sim(), creator.channelNumber(), "H.264 --> FLV", LocalDateTime.now(), creator.metadata(), fluxSink);
    }

    private void doErrorLogIfNecessary(Jt1078Request request, Jt1078PayloadType payloadType) {
        final Boolean flag = WARNING_FLAGS.get(payloadType);
        if (flag == null || !flag) {
            WARNING_FLAGS.put(payloadType, true);
            log.error("Unsupported payloadType : {}. sim = {}, channelNumber = {}, dataType = {}, request = {}", payloadType, request.sim(), request.channelNumber(), request.header().dataType(), request);
        }
    }

    @Override
    public void close() {
        try {
            super.close();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        try {
            this.flvEncoder.close();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
