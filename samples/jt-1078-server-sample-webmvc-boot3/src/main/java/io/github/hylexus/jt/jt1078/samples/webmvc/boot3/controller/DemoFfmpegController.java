package io.github.hylexus.jt.jt1078.samples.webmvc.boot3.controller;

import io.github.hylexus.jt.jt1078.spec.Jt1078Publisher;
import io.github.hylexus.jt.jt1078.spec.exception.Jt1078SessionDestroyException;
import io.github.hylexus.jt.jt1078.spec.impl.request.DefaultJt1078PayloadType;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078ChannelCollector;
import io.github.hylexus.jt.jt1078.support.extension.flatform.process.PlatformProcess;
import io.github.hylexus.jt.jt1078.support.extension.flatform.process.PlatformProcessManager;
import io.github.hylexus.jt.jt1078.support.extension.flatform.process.impl.DefaultPlatformProcessDescriber;
import io.github.hylexus.jt.utils.HexStringUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.scheduler.Schedulers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

@Slf4j
@RestController
public class DemoFfmpegController {

    private final Jt1078Publisher publisher;
    private final PlatformProcessManager platformProcessManager;

    public DemoFfmpegController(Jt1078Publisher publisher, PlatformProcessManager platformProcessManager) {
        this.publisher = publisher;
        this.platformProcessManager = platformProcessManager;
    }

    @RequestMapping("/jt1078/subscriber/ffmpeg")
    public Object subscribeForFfmpeg01(
            @RequestParam("sim") String sim,
            @RequestParam(value = "channel", defaultValue = "3") short channel,
            @RequestParam(value = "timeout", defaultValue = "10") long timeoutInSeconds) {
        final String rtmpEndpoint = "rtmp://127.0.0.1:1935/jt1078/" + sim + "_" + channel;
        // -i - : 表示从(新启动的Ffmpeg进程的)标准输入中读取数据
        // 实际上是通过 BuiltinFfmpegTransfer.writeData() 写数据给 Ffmpeg 进程的
        final String command = "ffmpeg -i - -vcodec libx264 -acodec copy -f flv " + rtmpEndpoint;

        final PlatformProcess platformProcess = this.platformProcessManager.createProcess(command);

        platformProcess.start();

        final Duration timeout = Duration.ofSeconds(timeoutInSeconds);
        this.publisher.subscribe(Jt1078ChannelCollector.RAW_DATA_COLLECTOR, sim, channel, timeout)
                .doOnError(Jt1078SessionDestroyException.class, e -> {
                    log.error("Session Destroy... subscribe complete");
                })
                .onErrorComplete(Jt1078SessionDestroyException.class)
                .publishOn(Schedulers.newBoundedElastic(2, 128, "demo"))
                // 仅仅过滤出 h264 数据
                .filter(it -> it.header().payloadType() == DefaultJt1078PayloadType.H264)
                // 当有数据到来时发送给 ffmpeg
                .doOnNext(subscription -> {
                    final byte[] data = subscription.payload();
                    log.info("Ffmpeg outbound: {}", HexStringUtils.bytes2HexString(data));
                    platformProcess.writeDataToStdIn(data, 0, data.length);
                })
                // 如果持续 timeoutInSeconds 秒之后一直没有数据
                .doOnError(TimeoutException.class, e -> {
                    log.info("超时关闭, command = {}", command);
                })
                .onErrorComplete(TimeoutException.class)
                // 最终关闭 ffmpeg 进程(Flux 正常结束、异常结束)
                .doFinally(signalType -> {
                    try {
                        platformProcess.destroy();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .subscribe();

        return Map.of(
                "processInfo", DefaultPlatformProcessDescriber.of(platformProcess),
                "rtmpEndpoint", rtmpEndpoint,
                "msg", "订阅成功；如果 " + timeout + " 内设备没有上传数据，当前订阅将会自动关闭。"
        );
    }

    @RequestMapping("/jt1078/subscriber/ffmpeg/processes/list")
    public List<DefaultPlatformProcessDescriber> ffmpegProcessList() {
        return this.platformProcessManager.list().collect(Collectors.toList());
    }

    @RequestMapping(value = "/jt1078/subscriber/ffmpeg/processes/{id}/destroy", produces = {TEXT_EVENT_STREAM_VALUE})
    public Object closeProcess(@PathVariable("id") String id) {
        this.platformProcessManager.destroyProcess(id);
        return "Success";
    }

    @RequestMapping(value = "/jt1078/subscriber/ffmpeg/processes/{id}/std-error", produces = {MediaType.TEXT_EVENT_STREAM_VALUE})
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
