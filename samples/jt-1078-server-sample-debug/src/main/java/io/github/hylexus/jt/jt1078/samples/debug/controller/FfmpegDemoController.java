package io.github.hylexus.jt.jt1078.samples.debug.controller;

import io.github.hylexus.jt.jt1078.spec.Jt1078Publisher;
import io.github.hylexus.jt.jt1078.spec.impl.request.DefaultJt1078PayloadType;
import io.github.hylexus.jt.jt1078.spec.impl.subscription.BuiltinFfmpegTransfer;
import io.github.hylexus.jt.utils.HexStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    public FfmpegDemoController(Jt1078Publisher publisher) {
        this.publisher = publisher;
    }

    @RequestMapping("/ffmpeg/demo01")
    public Object subscribeForFfmpeg(
            @RequestParam("sim") String sim,
            @RequestParam(value = "channel", defaultValue = "3") short channel,
            @RequestParam(value = "timeoutInSeconds", defaultValue = "10") long timeoutInSeconds) {
        final String rtmpEndpoint = "rtmp://127.0.0.1:1935/jt1078/" + sim + "_" + channel;
        // -i - : 表示从(新启动的Ffmpeg进程的)标准输入中读取数据
        // 实际上是通过 BuiltinFfmpegTransfer.writeData() 写数据给 Ffmpeg 进程的
        final String command = "ffmpeg -i - -vcodec libx264 -acodec copy -f flv " + rtmpEndpoint;

        final BuiltinFfmpegTransfer subscriber = new BuiltinFfmpegTransfer(command);

        // 另起一个线程来启动 ffmpeg 的进程
        // 你也可以提交到你自定义的线程池中
        new Thread(subscriber).start();

        // new Thread(() -> this.logFfmpegProcessOutput(subscriber)).start();

        final Duration timeout = Duration.ofSeconds(timeoutInSeconds);
        this.publisher.subscribe(sim, channel, timeout)
                // 仅仅过滤出 h264 数据
                .filter(it -> it.getRequest().payloadType() == DefaultJt1078PayloadType.H264)
                // 当有数据到来时发送给 ffmpeg
                .doOnNext(subscription -> {
                    final byte[] data = new byte[subscription.getRequest().body().readableBytes()];
                    subscription.getRequest().body().getBytes(0, data);
                    log.info("Ffmpeg outbound: {}", HexStringUtils.bytes2HexString(data));
                    subscriber.writeData(data, 0, data.length);
                })
                // 如果持续 timeoutInSeconds 秒之后一直没有数据
                .doOnError(TimeoutException.class, e -> {
                    log.info("超时关闭, command = {}", command);
                })
                .onErrorComplete(TimeoutException.class)
                // 最终关闭 ffmpeg 进程(Flux 正常结束、异常结束)
                .doFinally(signalType -> {
                    try {
                        subscriber.close();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                // 这里就在 "project-reactor" 的默认线程上订阅了
                // 你也可以在你自己的线程池上订阅
                .subscribe();

        return Map.of(
                "ffmpegCommand", command,
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
