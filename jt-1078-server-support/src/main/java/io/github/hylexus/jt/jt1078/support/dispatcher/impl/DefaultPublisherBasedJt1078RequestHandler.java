package io.github.hylexus.jt.jt1078.support.dispatcher.impl;

import io.github.hylexus.jt.jt1078.spec.Jt1078PublisherInternal;
import io.github.hylexus.jt.jt1078.spec.Jt1078Request;
import io.github.hylexus.jt.jt1078.support.dispatcher.Jt1078RequestHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class DefaultPublisherBasedJt1078RequestHandler implements Jt1078RequestHandler {

    private final Jt1078PublisherInternal jt1078PublisherManager;

    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(10, new DefaultThreadFactory("publisher"));

    public DefaultPublisherBasedJt1078RequestHandler(Jt1078PublisherInternal jt1078PublisherManager) {
        this.jt1078PublisherManager = jt1078PublisherManager;
    }

    @Override
    public boolean support(Jt1078Request request) {
        return true;
    }

    @Override
    public void handle(Jt1078Request request) {

        try {
            jt1078PublisherManager.publish(request);
        } finally {
            request.release();
        }
    }

}
