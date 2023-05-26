package io.github.hylexus.jt.jt1078.spec;

import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * @author hylexus
 */
public interface Jt1078Publisher {

    // TODO 这个方法写成通用的订阅(hdfs, nginx, ...)
    Flux<Jt1078Subscription> subscribeForEver();

    Flux<Jt1078Subscription> subscribe(Duration timeout);

    Flux<Jt1078Subscription> subscribe(String sim, short channelNumber, Duration timeout);
}
