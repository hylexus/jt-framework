package io.github.hylexus.jt.jt1078.samples.webmvc.boot3.subscriber;

import io.github.hylexus.jt.annotation.DebugOnly;
import io.github.hylexus.jt.common.JtCommonUtils;
import io.github.hylexus.jt.jt1078.spec.*;
import io.github.hylexus.jt.jt1078.spec.exception.Jt1078SessionDestroyException;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078ChannelCollector;
import io.github.hylexus.jt.utils.HexStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import javax.annotation.Nullable;
import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * 用来收集数据--Flv格式(仅仅用来测试)
 * <p>
 * 用来收集数据--Flv格式(仅仅用来测试)
 * <p>
 * 用来收集数据--Flv格式(仅仅用来测试)
 */
@Slf4j
@Component
@DebugOnly
public class LocalDebugFlvSubscriber implements Jt1078SessionEventListener, DisposableBean {

    static Scheduler SCHEDULER = Schedulers.newBoundedElastic(10, 128, "Demo");
    private final String targetDir;

    private final Map<String, OutputStream> registry = new HashMap<>();

    private final Jt1078Publisher publisher;

    public LocalDebugFlvSubscriber(@Value("${local-debug.flv-data-dump-to-file.parent-dir}") String targetDir, Jt1078Publisher publisher) {
        this.targetDir = targetDir;
        this.publisher = publisher;
        final File file = new File(targetDir);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    @Override
    public void onSessionAdd(@Nullable Jt1078Session session) {
        if (session == null) {
            return;
        }
        final OutputStream outputStream = getOutputStream(session);

        new Thread(() -> {
            this.publisher
                    .subscribe(Jt1078ChannelCollector.H264_TO_FLV_COLLECTOR, Jt1078SubscriberCreator.builder()
                            .sim(session.sim())
                            .channelNumber(session.channelNumber())
                            .timeout(Duration.ofSeconds(100))
                            .metadata(Map.of("desc", "仅仅用来本地收集Flv数据测试"))
                            .build()
                    )
                    // .subscribe(Jt1078ChannelCollector.H264_TO_FLV_COLLECTOR, session.sim(), session.channelNumber(), Duration.ofMinutes(100))
                    .publishOn(SCHEDULER)
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
                        writeInternal(outputStream, it.payload());
                        // log.info("local file dump ... {} --> ::: {}", it.type(), hex);
                    })
                    .subscribe();
        }).start();
    }

    static void writeInternal(OutputStream outputStream, byte[] bytes) {
        try {
            outputStream.write(bytes);
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private synchronized OutputStream getOutputStream(Jt1078Session session) {
        final String key = key(session);
        final OutputStream metadata = registry.get(key);
        if (metadata != null) {
            JtCommonUtils.close(metadata);
        }

        final OutputStream newOutputStream = this.createNewOutputStream(session.sim(), key);
        this.registry.put(key, newOutputStream);
        return newOutputStream;
    }

    private OutputStream createNewOutputStream(String sim, String key) {
        final String fileName = this.targetDir + File.separator + sim + File.separator + key + File.separator + (DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss").format(LocalDateTime.now())) + ".flv";
        final File file = new File(fileName);
        try {
            file.getParentFile().mkdirs();
            return new FileOutputStream(fileName);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void destroy() throws Exception {
        this.registry.forEach((k, v) -> {
            JtCommonUtils.close(v);
        });
    }

    String key(Jt1078Session session) {
        return session.sim() + "-" + session.channelNumber();
    }

    String key(Jt1078Request session) {
        return session.sim() + "-" + session.channelNumber();
    }
}
