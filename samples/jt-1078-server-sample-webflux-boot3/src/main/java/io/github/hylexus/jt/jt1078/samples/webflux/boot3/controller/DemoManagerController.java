package io.github.hylexus.jt.jt1078.samples.webflux.boot3.controller;

import io.github.hylexus.jt.jt1078.spec.Jt1078PublisherManager;
import io.github.hylexus.jt.jt1078.spec.Jt1078SubscriberDescriptor;
import io.github.hylexus.jt.jt1078.support.extension.flatform.process.PlatformProcess;
import io.github.hylexus.jt.jt1078.support.extension.flatform.process.PlatformProcessManager;
import io.github.hylexus.jt.jt1078.support.extension.flatform.process.impl.DefaultPlatformProcessDescriber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

@Slf4j
@RestController
public class DemoManagerController {
    private final Jt1078PublisherManager publisherManager;
    private final PlatformProcessManager platformProcessManager;

    public DemoManagerController(Jt1078PublisherManager publisherManager, PlatformProcessManager platformProcessManager) {
        this.publisherManager = publisherManager;
        this.platformProcessManager = platformProcessManager;
    }

    @RequestMapping("/subscriber/list")
    public List<Jt1078SubscriberDescriptor> publisherList() {
        return this.publisherManager.list().collect(Collectors.toList());
    }

    @RequestMapping("/ffmpeg/processes/list")
    public List<DefaultPlatformProcessDescriber> ffmpegProcessList() {
        return this.platformProcessManager.list().collect(Collectors.toList());
    }

    @RequestMapping(value = "/ffmpeg/processes/{id}/destroy", produces = {TEXT_EVENT_STREAM_VALUE})
    public Object closeProcess(@PathVariable("id") String id) {
        this.platformProcessManager.destroyProcess(id);
        return "Success";
    }

    @RequestMapping(value = "/ffmpeg/processes/{id}/std-error", produces = {TEXT_EVENT_STREAM_VALUE})
    public Flux<String> readSteOut(@PathVariable("id") String id) {

        final PlatformProcess platformProcess = this.platformProcessManager.getProcess(id).orElseThrow();

        // 这个 Stream 不需要关闭
        final InputStream errorStream = platformProcess.process().getErrorStream();

        return Flux.create(fluxSink -> {
            new Thread(() -> {
                String line;
                final BufferedReader reader = new BufferedReader(new InputStreamReader(errorStream));
                try {
                    while ((line = reader.readLine()) != null) {
                        fluxSink.next(line);
                        log.info("next: {}", line);
                    }
                    fluxSink.complete();
                    log.info("Complete");
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                    fluxSink.error(e);
                }
            }).start();
        });
    }

}
