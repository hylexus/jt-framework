package io.github.hylexus.jt.jt1078.spec;

import java.util.Optional;

/**
 * @author hylexus
 */
public interface Jt1078PublisherManager {

    Optional<Jt1078Publisher> getPublisher(Jt1078SubscriptionSelector identifier);

    Jt1078Publisher getOrCreatePublisher(Jt1078SubscriptionSelector identifier);

    void removePublisher(Jt1078SubscriptionSelector identifier);

}
