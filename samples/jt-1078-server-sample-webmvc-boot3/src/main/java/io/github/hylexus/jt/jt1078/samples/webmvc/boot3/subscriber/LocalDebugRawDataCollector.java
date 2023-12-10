package io.github.hylexus.jt.jt1078.samples.webmvc.boot3.subscriber;

import io.github.hylexus.jt.annotation.DebugOnly;
import io.github.hylexus.jt.common.JtCommonUtils;
import io.github.hylexus.jt.jt1078.spec.Jt1078Request;
import io.github.hylexus.jt.jt1078.spec.Jt1078RequestLifecycleListener;
import io.github.hylexus.jt.jt1078.spec.Jt1078Session;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionEventListener;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static io.github.hylexus.jt.jt1078.samples.webmvc.boot3.subscriber.LocalDebugFlvSubscriber.writeInternal;


/**
 * 用来收集设备数据--裸流(仅仅用于测试)
 * <p>
 * 用来收集设备数据--裸流(仅仅用于测试)
 * <p>
 * 用来收集设备数据--裸流(仅仅用于测试)
 */
@Slf4j
@Component
@DebugOnly
public class LocalDebugRawDataCollector
        implements Jt1078RequestLifecycleListener, Jt1078SessionEventListener, DisposableBean {

    private final String targetDir;

    public LocalDebugRawDataCollector(@Value("${local-debug.raw-data-dump-to-file.parent-dir}") String targetDir) {
        this.targetDir = targetDir;
    }

    Map<String, OutputStream> registry = new HashMap<>();

    @Override
    public boolean beforeDispatch(Jt1078Request request, Channel channel) {
        try {
            final ByteBuf rawByteBuf = request.rawByteBuf();
            if (rawByteBuf == null) {
                return true;
            }
            final OutputStream outputStream = this.registry.get(key(request));
            if (outputStream == null) {
                return true;
            }
            final byte[] bytes = JtCommonUtils.getBytes(rawByteBuf);
            writeInternal(outputStream, new byte[]{0x30, 0x31, 0x63, 0x64});
            writeInternal(outputStream, bytes);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return true;
        }
    }

    @Override
    public synchronized void onSessionAdd(@Nullable Jt1078Session session) {
        if (session == null) {
            return;
        }
        final String key = key(session);

        final OutputStream outputStream = this.registry.get(key);
        if (outputStream != null) {
            JtCommonUtils.close(outputStream);
        }

        this.registry.put(key, this.createNewOutputStream(session.sim(), key));
    }

    String key(Jt1078Session session) {
        return session.sim() + "-" + session.channelNumber();
    }

    String key(Jt1078Request session) {
        return session.sim() + "-" + session.channelNumber();
    }

    private OutputStream createNewOutputStream(String sim, String key) {
        final String fileName = this.targetDir + File.separator + sim + File.separator + key + File.separator + (DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss").format(LocalDateTime.now())) + ".dat";
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
}
