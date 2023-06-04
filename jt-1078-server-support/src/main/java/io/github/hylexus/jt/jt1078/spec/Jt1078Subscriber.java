package io.github.hylexus.jt.jt1078.spec;

import reactor.core.publisher.Flux;

public interface Jt1078Subscriber<S extends Jt1078Subscription> {

    /**
     * 这个 uuid 可以用来从外部关闭当前订阅
     *
     * @return uuid
     */
    String id();

    /**
     * @return 当前订阅的数据流
     */
    Flux<S> dataStream();

}
