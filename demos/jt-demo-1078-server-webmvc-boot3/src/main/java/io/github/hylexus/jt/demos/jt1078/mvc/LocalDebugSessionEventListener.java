package io.github.hylexus.jt.demos.jt1078.mvc;

import io.github.hylexus.jt.annotation.DebugOnly;
import io.github.hylexus.jt.jt1078.spec.Jt1078Publisher;
import io.github.hylexus.jt.jt1078.spec.Jt1078Request;
import io.github.hylexus.jt.jt1078.spec.Jt1078Session;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionEventListener;
import io.github.hylexus.jt.jt1078.spec.exception.Jt1078SessionDestroyException;
import io.github.hylexus.jt.jt1078.support.codec.Jt1078ChannelCollector;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.github.hylexus.oaks.utils.Numbers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import javax.annotation.Nullable;
import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
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
// @Component
@DebugOnly
public class LocalDebugSessionEventListener implements Jt1078SessionEventListener, DisposableBean {

    static Scheduler SCHEDULER = Schedulers.newBoundedElastic(10, 128, "Demo");
    private final String targetDir;

    private final Map<String, OutputStream> registry = new HashMap<>();

    private final Jt1078Publisher publisher;

    public LocalDebugSessionEventListener(@Value("${local-debug.flv-data-dump-to-file.parent-dir}") String targetDir, Jt1078Publisher publisher) {
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
                    .subscribe(Jt1078ChannelCollector.H264_TO_FLV_COLLECTOR, session.sim(), session.channelNumber(), Duration.ofSeconds(100))
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
            close(metadata);
        }

        final OutputStream newOutputStream = this.createNewOutputStream(key);
        this.registry.put(key, newOutputStream);
        return newOutputStream;
    }

    private OutputStream createNewOutputStream(String key) {
        final String fileName = this.targetDir + File.separator + key + File.separator + (DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now())) + ".flv";
        final File file = new File(fileName);
        try {
            file.getParentFile().mkdirs();
            return new FileOutputStream(fileName);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    static int findNextIndex(String key, String targetDir) {
        final String dirName = targetDir + File.separator + key;
        final File dir = new File(dirName);
        if (!dir.exists()) {
            return 0;
        }
        // ${sim-channelNumber}_${index}.flv
        final String[] list = dir.list();
        if (list == null || list.length == 0) {
            return 0;
        }
        return Arrays.stream(list).map(it -> {
            final String[] a1 = it.split("_");
            final String[] a2 = a1[1].split("\\.");
            return Numbers.parseInteger(a2[0]).orElseGet(() -> {
                new File(it).delete();
                return -1;
            });
        }).max(Integer::compareTo).orElse(-1) + 1;
    }

    @Override
    public void destroy() throws Exception {
        this.registry.forEach((k, v) -> {
            close(v);
        });
    }

    static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignore) {
                // ignore
            }
        }
    }

    String key(Jt1078Session session) {
        return session.sim() + "-" + session.channelNumber();
    }

    String key(Jt1078Request session) {
        return session.sim() + "-" + session.channelNumber();
    }
}
