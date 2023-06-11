package io.github.hylexus.jt.jt1078.samples.webflux.boot3.controller;

import io.github.hylexus.jt.jt1078.spec.Jt1078PublisherManager;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionManager;
import io.github.hylexus.jt.jt1078.spec.Jt1078SubscriberDescriptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class Jt1078DebugController {
    private final Jt1078PublisherManager publisherManager;
    private final Jt1078SessionManager sessionManager;

    public Jt1078DebugController(Jt1078PublisherManager publisherManager, Jt1078SessionManager sessionManager) {
        this.publisherManager = publisherManager;
        this.sessionManager = sessionManager;
    }

    @RequestMapping("/jt1078/subscriber/list")
    public List<Jt1078SubscriberDescriptor> publisherList() {
        return this.publisherManager.list().collect(Collectors.toList());
    }

    @RequestMapping("/jt1078/session/list")
    public List<Jt1078SessionVo> sessionList() {
        return this.sessionManager.list()
                .map(it -> new Jt1078SessionVo(it.sessionId(), it.sim(), new Date(it.lastCommunicateTimestamp())))
                .collect(Collectors.toList());
    }

    public record Jt1078SessionVo(String id, String sim, Date lastCommunicateTime) {
    }
}
