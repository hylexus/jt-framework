package io.github.hylexus.jt.jt1078.support.exception;

import io.github.hylexus.jt.jt1078.spec.Jt1078PayloadType;
import io.github.hylexus.jt.jt1078.spec.Jt1078SubscriptionType;

/**
 * @author hylexus
 */
public class Jt1078CollectorNotFoundException extends AbstractJt1078Exception {
    private final Jt1078PayloadType payloadType;
    private final Jt1078SubscriptionType subscriptionType;

    public Jt1078CollectorNotFoundException(Jt1078PayloadType payloadType, Jt1078SubscriptionType subscriptionType) {
        this.payloadType = payloadType;
        this.subscriptionType = subscriptionType;
    }

    public Jt1078CollectorNotFoundException(String message, Jt1078PayloadType payloadType, Jt1078SubscriptionType subscriptionType) {
        super(message);
        this.payloadType = payloadType;
        this.subscriptionType = subscriptionType;
    }

    public Jt1078CollectorNotFoundException(String message, Throwable cause, Jt1078PayloadType payloadType, Jt1078SubscriptionType subscriptionType) {
        super(message, cause);
        this.payloadType = payloadType;
        this.subscriptionType = subscriptionType;
    }

    public Jt1078CollectorNotFoundException(Throwable cause, Jt1078PayloadType payloadType, Jt1078SubscriptionType subscriptionType) {
        super(cause);
        this.payloadType = payloadType;
        this.subscriptionType = subscriptionType;
    }

    public Jt1078CollectorNotFoundException(
            String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace,
            Jt1078PayloadType payloadType, Jt1078SubscriptionType subscriptionType) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.payloadType = payloadType;
        this.subscriptionType = subscriptionType;
    }
}
