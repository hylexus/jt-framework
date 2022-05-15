package io.github.hylexus.jt.jt1078.support.codec;

import io.github.hylexus.jt.jt1078.spec.Jt1078SubscriptionSelector;

import java.util.Optional;

/**
 * @author hylexus
 */
public interface Jt1078CollectorFactory {

    Optional<Jt1078Collector> createCollector(Jt1078SubscriptionSelector selector);

}
