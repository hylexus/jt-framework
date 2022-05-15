package io.github.hylexus.jt.jt1078.spec.impl;

import io.github.hylexus.jt.jt1078.spec.Jt1078Publisher;
import io.github.hylexus.jt.jt1078.spec.Jt1078Subscription;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078Sink;

/**
 * @author hylexus
 */
public interface InternalJt1078Publisher extends Jt1078Publisher, Jt1078Sink<Jt1078Subscription> {
}
