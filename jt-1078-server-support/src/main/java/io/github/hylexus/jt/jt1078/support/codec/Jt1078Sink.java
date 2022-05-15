package io.github.hylexus.jt.jt1078.support.codec;

import io.github.hylexus.jt.jt1078.spec.Jt1078Subscription;

/**
 * @author hylexus
 */
public interface Jt1078Sink<T extends Jt1078Subscription> {

    void next(T subscription);

    void error(Throwable error);

    void complete();

}
