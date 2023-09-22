package io.github.hylexus.jt.jt1078.samples.webmvc.boot3.controller;

import io.github.hylexus.jt.jt1078.spec.Jt1078SubscriberManager;
import io.github.hylexus.jt.jt1078.spec.Jt1078SubscriberDescriptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class DemoSubscriberController {
    private final Jt1078SubscriberManager publisherManager;

    public DemoSubscriberController(Jt1078SubscriberManager publisherManager) {
        this.publisherManager = publisherManager;
    }

    @RequestMapping("/publisher/list")
    public List<Jt1078SubscriberDescriptor> publisherList() {
        return this.publisherManager.list().collect(Collectors.toList());
    }

}
