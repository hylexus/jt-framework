package io.github.hylexus.jt.jt1078.support.extension.flatform.process;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

public interface PlatformProcess extends Runnable {

    String uuid();

    Process process();

    String command();

    void destroy(Consumer<PlatformProcess> action);

    default void destroy() {
        this.destroy(platformProcess -> {
        });
    }

    void start();

    void start(ExecutorService executorService);

    boolean running();

    default InputStream errorStream() {
        final Process process = this.process();
        if (process == null) {
            return null;
        }
        return process.getErrorStream();
    }

    default InputStream inputStream() {
        final Process process = this.process();
        if (process == null) {
            return null;
        }
        return process.getInputStream();
    }

    default OutputStream outputStream() {
        final Process process = this.process();
        if (process == null) {
            return null;
        }

        return process.getOutputStream();
    }

    /**
     * @see System#in
     */
    default void writeDataToStdIn(byte[] data, int offset, int length) {
        try {
            final OutputStream stream = outputStream();
            if (stream == null) {
                throw new IllegalStateException();
            }
            stream.write(data, offset, length);
            stream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
