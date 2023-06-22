package io.github.hylexus.jt.jt1078.support.codec.impl.collector;

import io.github.hylexus.jt.jt1078.spec.impl.subscription.ByteArrayJt1078Subscription;
import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.FluxSink;

@Getter
public class H264ToFlvSubscriber extends DefaultByteArraySubscriber {

    @Setter
    private boolean flvHeaderSent;
    @Setter
    private boolean lastIFrameSent;

    public H264ToFlvSubscriber(String id, String desc, FluxSink<ByteArrayJt1078Subscription> sink) {
        super(id, desc, sink);
    }
}
