package io.github.hylexus.jt.jt1078.spec.impl.subscription;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class BuiltinFfmpegTransfer implements Runnable, AutoCloseable {
    private final Process process;
    private final String command;
    private final AtomicBoolean running = new AtomicBoolean(false);

    public BuiltinFfmpegTransfer(String command) {
        this.command = command;
        this.process = this.createProcess();
    }

    public void writeData(byte[] data, int offset, int length) {
        try {
            getOutputStream().write(data, offset, length);
            getOutputStream().flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        if (!this.running.compareAndSet(true, false)) {
            return;
        }
        if (this.process != null) {
            this.process.destroy();
            log.info("{} closed. Description: {}", this.getClass().getSimpleName(), this);
        }
    }

    @Override
    public void run() {
        if (!this.running.compareAndSet(false, true)) {
            return;
        }
        try {
            final int exitedCode = this.process.waitFor();
            log.info("{} exited with code : {}", this, exitedCode);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private Process createProcess() {
        try {
            // return new ProcessBuilder()
            //         .redirectErrorStream(true)
            //         .command(this.command)
            //         .start();
            return Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public OutputStream getOutputStream() {
        return process.getOutputStream();
    }

    public InputStream getInputStream() {
        return process.getInputStream();
    }

    public InputStream getErrorStream() {
        return process.getErrorStream();
    }

    public Process getProcess() {
        return process;
    }

    @Override
    public String toString() {
        return "BuiltinFfmpegSubscriber{"
                + "command='" + command + '\''
                + '}';
    }
}
