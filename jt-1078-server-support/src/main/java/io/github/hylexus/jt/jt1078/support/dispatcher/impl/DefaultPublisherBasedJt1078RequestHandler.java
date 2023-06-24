package io.github.hylexus.jt.jt1078.support.dispatcher.impl;

import io.github.hylexus.jt.jt1078.spec.Jt1078PublisherInternal;
import io.github.hylexus.jt.jt1078.spec.Jt1078Request;
import io.github.hylexus.jt.jt1078.support.dispatcher.Jt1078RequestHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultPublisherBasedJt1078RequestHandler implements Jt1078RequestHandler {

    private final Jt1078PublisherInternal jt1078PublisherManager;

    public DefaultPublisherBasedJt1078RequestHandler(Jt1078PublisherInternal jt1078PublisherManager) {
        this.jt1078PublisherManager = jt1078PublisherManager;
    }

    @Override
    public boolean support(Jt1078Request request) {
        return true;
    }

    @Override
    public void handle(Jt1078Request request) {
        jt1078PublisherManager.publish(request);
    }

}
