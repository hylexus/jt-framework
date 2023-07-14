package io.github.hylexus.jt.jt1078.support.codec.impl.collector;

import io.github.hylexus.jt.jt1078.spec.Jt1078Request;
import io.github.hylexus.jt.jt1078.spec.Jt1078Subscription;
import io.github.hylexus.jt.jt1078.spec.impl.subscription.ByteArrayJt1078Subscription;
import io.github.hylexus.jt.jt1078.spec.impl.subscription.DefaultJt1078SubscriptionType;
import io.github.hylexus.jt.jt1078.support.extension.flv.FlvHeader;
import io.github.hylexus.jt.jt1078.support.extension.flv.impl.DefaultFlvEncoder;
import io.github.hylexus.jt.utils.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import reactor.core.publisher.FluxSink;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadFactory;

/**
 * H.264 --> FLV
 */
public class H264ToFlvJt1078ChannelCollector
        extends AbstractAsyncChannelCollector<ByteArrayJt1078Subscription, H264ToFlvSubscriber> {

    private final DefaultFlvEncoder flvEncoder;

    public H264ToFlvJt1078ChannelCollector(ThreadFactory threadFactory) {
        super(threadFactory);
        this.flvEncoder = new DefaultFlvEncoder();
    }

    @Override
    protected void doCollect(Jt1078Request request) {
        final List<ByteBuf> bufList = flvEncoder.encode(request);
        for (final ByteBuf byteBuf : bufList) {
            try {
                final ByteArrayJt1078Subscription subscription = Jt1078Subscription.forByteArray(DefaultJt1078SubscriptionType.FLV, ByteBufUtils.getBytes(byteBuf));
                for (final H264ToFlvSubscriber subscriber : this.subscriberMapping.values()) {
                    final FluxSink<ByteArrayJt1078Subscription> sink = subscriber.sink();

                    if (!subscriber.isFlvHeaderSent()) {
                        final ByteBuf basicFrame = flvEncoder.getFlvBasicFrame();
                        if (basicFrame != null) {
                            // header
                            sink.next(Jt1078Subscription.forByteArray(DefaultJt1078SubscriptionType.FLV, FlvHeader.of(true, false).toBytes(true)));
                            // sps+pps
                            sink.next(Jt1078Subscription.forByteArray(DefaultJt1078SubscriptionType.FLV, basicFrame));
                            subscriber.setFlvHeaderSent(true);
                        }
                    }

                    if (!subscriber.isLastIFrameSent()) {
                        final ByteBuf lastIFrame = flvEncoder.getLastIFrame();
                        if (lastIFrame != null) {
                            sink.next(Jt1078Subscription.forByteArray(DefaultJt1078SubscriptionType.FLV, lastIFrame));
                            subscriber.setLastIFrameSent(true);
                        }
                    }

                    sink.next(subscription);
                }
            } finally {
                byteBuf.release();
            }
        }
    }


    @Override
    protected H264ToFlvSubscriber createSubscribe(String uuid, String sim, short channelNumber, FluxSink<ByteArrayJt1078Subscription> fluxSink) {
        return new H264ToFlvSubscriber(uuid, sim, channelNumber, "H.264 --> FLV", LocalDateTime.now(), fluxSink);
    }

}
