package io.github.hylexus.jt.jt1078.samples.webmvc.boot3.controller;

import io.github.hylexus.jt.jt1078.spec.Jt1078PublisherManager;
import io.github.hylexus.jt.jt1078.spec.Jt1078SubscriberDescriptor;
import io.github.hylexus.jt.jt1078.support.extension.flatform.process.PlatformProcess;
import io.github.hylexus.jt.jt1078.support.extension.flatform.process.PlatformProcessManager;
import io.github.hylexus.jt.jt1078.support.extension.flatform.process.impl.DefaultPlatformProcessDescriber;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class DemoManagerController {
    private final Jt1078PublisherManager publisherManager;
    private final PlatformProcessManager platformProcessManager;

    public DemoManagerController(Jt1078PublisherManager publisherManager, PlatformProcessManager platformProcessManager) {
        this.publisherManager = publisherManager;
        this.platformProcessManager = platformProcessManager;
    }

    @RequestMapping("/publisher/list")
    public List<Jt1078SubscriberDescriptor> publisherList() {
        return this.publisherManager.list().collect(Collectors.toList());
    }

    @RequestMapping("/ffmpeg/processes/list")
    public List<DefaultPlatformProcessDescriber> ffmpegProcessList() {
        return this.platformProcessManager.list().collect(Collectors.toList());
    }

    // @RequestMapping(value = "/ffmpeg/processes/{id}/std-error", produces = {TEXT_EVENT_STREAM_VALUE})
    @RequestMapping(value = "/ffmpeg/processes/{id}/std-error", produces = {MediaType.TEXT_EVENT_STREAM_VALUE})
    public SseEmitter readSteOut(@PathVariable("id") String id, HttpServletResponse response) throws IOException {
        final SseEmitter sseEmitter = new SseEmitter(0L);
        final PlatformProcess platformProcess = this.platformProcessManager.getProcess(id).orElseThrow();
        response.setContentType(MediaType.TEXT_EVENT_STREAM_VALUE);
        // 这个 Stream 不需要关闭
        new Thread(() -> {
            final InputStream errorStream = platformProcess.process().getErrorStream();

            String line;
            final BufferedReader reader = new BufferedReader(new InputStreamReader(errorStream));
            try {
                while ((line = reader.readLine()) != null) {
                    sseEmitter.send(line);
                }
                log.info("Complete");
                sseEmitter.complete();
            } catch (Throwable e) {
                log.error(e.getMessage(), e);
                sseEmitter.completeWithError(e);
            }
        }).start();

        return sseEmitter;
    }

}
