package io.github.hylexus.jt.jt1078.samples.webflux.boot3.controller;

import io.github.hylexus.jt.jt1078.spec.Jt1078PublisherManager;
import io.github.hylexus.jt.jt1078.spec.Jt1078SubscriberDescriptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class DemoSubscriberController {
    private final Jt1078PublisherManager publisherManager;


    public DemoSubscriberController(Jt1078PublisherManager publisherManager) {
        this.publisherManager = publisherManager;
    }

    @RequestMapping("/jt1078/subscriber/list")
    public List<Jt1078SubscriberDescriptor> publisherList() {
        return this.publisherManager.list().collect(Collectors.toList());
    }

}
