package io.github.hylexus.jt.jt1078.samples.webmvc.boot3.debug;

import io.github.hylexus.jt.jt1078.samples.webmvc.boot3.config.WebSocketConfig;
import io.github.hylexus.jt.jt1078.spec.Jt1078Publisher;
import io.github.hylexus.jt.jt1078.spec.exception.Jt1078SessionDestroyException;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078ChannelCollector;
import io.github.hylexus.jt.jt1078.support.codec.impl.collector.H264ToFlvJt1078ChannelCollector;
import io.github.hylexus.jt.utils.HexStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
public class FlvToFileSubscriber implements InitializingBean {
    private final Jt1078Publisher publisher;
    private final OutputStream outputStream1;
    private final OutputStream outputStream2;

    public FlvToFileSubscriber(Jt1078Publisher publisher, @Value("${local-debug.flv-to-file.parent-dir}") String baseDir) {
        this.publisher = publisher;
        try {
            this.outputStream1 = new FileOutputStream(baseDir + "/1.flv");
            this.outputStream2 = new FileOutputStream(baseDir + "/2.flv");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void afterPropertiesSet() {
        new Thread(() -> {
            this.publisher
                    .subscribe(Jt1078ChannelCollector.H264_TO_FLV_COLLECTOR, "018930946552", (short) 3, Duration.ofSeconds(100))
                    .publishOn(WebSocketConfig.SCHEDULER)
                    .onErrorComplete(Jt1078SessionDestroyException.class)
                    .onErrorComplete(TimeoutException.class)
                    .doOnError(TimeoutException.class, e -> {
                        log.error("取消订阅(超时)");
                    })
                    .doOnError(Jt1078SessionDestroyException.class, e -> {
                        log.info("取消订阅(Session销毁)");
                    })
                    .doOnNext((it) -> {
                        final String hex = HexStringUtils.bytes2HexString(it.payload());
                        this.writeInternal(this.outputStream1, it.payload());
                        log.info("FlvToFileSubscriber1 ... {} --> ::: {}", it.type(), hex);
                    })
                    .subscribe();
        }, "flv-to-file-01").start();

        new Thread(() -> {
            this.publisher
                    .subscribe(H264ToFlvJt1078ChannelCollector.class, "018930946552", (short) 3, Duration.ofSeconds(100))
                    .publishOn(WebSocketConfig.SCHEDULER)
                    .onErrorComplete(Jt1078SessionDestroyException.class)
                    .doOnError(TimeoutException.class, e -> {
                        log.error("取消订阅(超时)");
                    })
                    .doOnError(Jt1078SessionDestroyException.class, e -> {
                        log.info("取消订阅(Session销毁)");
                    })
                    .doOnNext((it) -> {
                        final String hex = HexStringUtils.bytes2HexString(it.payload());
                        this.writeInternal(this.outputStream2, it.payload());
                        log.info("FlvToFileSubscriber2 ... {} --> ::: {}", it.type(), hex);
                    })
                    .subscribe();
        }, "flv-to-file-02").start();
    }

    private void writeInternal(OutputStream outputStream, byte[] bytes) {
        try {
            outputStream.write(bytes);
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
