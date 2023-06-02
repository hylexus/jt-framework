package io.github.hylexus.jt.jt1078.samples.webmvc.boot3.controller;

import io.github.hylexus.jt.jt1078.spec.Jt1078Publisher;
import io.github.hylexus.jt.jt1078.spec.impl.request.DefaultJt1078PayloadType;
import io.github.hylexus.jt.jt1078.spec.impl.subscription.BuiltinFfmpegTransfer;
import io.github.hylexus.jt.jt1078.support.extension.flatform.process.PlatformProcess;
import io.github.hylexus.jt.jt1078.support.extension.flatform.process.PlatformProcessManager;
import io.github.hylexus.jt.jt1078.support.extension.flatform.process.impl.DefaultPlatformProcessDescriber;
import io.github.hylexus.jt.utils.HexStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.scheduler.Schedulers;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Slf4j
@RestController
public class FfmpegDemoController {

    private final Jt1078Publisher publisher;
    private final PlatformProcessManager platformProcessManager;

    public FfmpegDemoController(Jt1078Publisher publisher, PlatformProcessManager platformProcessManager) {
        this.publisher = publisher;
        this.platformProcessManager = platformProcessManager;
    }

    @RequestMapping("/ffmpeg/demo01")
    public Object subscribeForFfmpeg01(
            @RequestParam("sim") String sim,
            @RequestParam(value = "channel", defaultValue = "3") short channel,
            @RequestParam(value = "timeoutInSeconds", defaultValue = "10") long timeoutInSeconds) {
        final String rtmpEndpoint = "rtmp://127.0.0.1:1935/jt1078/" + sim + "_" + channel;
        // -i - : 表示从(新启动的Ffmpeg进程的)标准输入中读取数据
        // 实际上是通过 BuiltinFfmpegTransfer.writeData() 写数据给 Ffmpeg 进程的
        final String command = "ffmpeg -i - -vcodec libx264 -acodec copy -f flv " + rtmpEndpoint;

        final PlatformProcess platformProcess = this.platformProcessManager.createProcess(command);

        platformProcess.start();

        final Duration timeout = Duration.ofSeconds(timeoutInSeconds);
        this.publisher.subscribe(sim, channel, timeout)
                .publishOn(Schedulers.newBoundedElastic(2, 128, "demo"))
                // 仅仅过滤出 h264 数据
                .filter(it -> it.getRequest().payloadType() == DefaultJt1078PayloadType.H264)
                // 当有数据到来时发送给 ffmpeg
                .doOnNext(subscription -> {
                    final byte[] data = new byte[subscription.getRequest().body().readableBytes()];
                    subscription.getRequest().body().getBytes(0, data);
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

    private void logFfmpegProcessOutput(BuiltinFfmpegTransfer subscriber) {
        try (
                final BufferedInputStream inputStream = new BufferedInputStream(subscriber.getErrorStream());
                final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));) {

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
