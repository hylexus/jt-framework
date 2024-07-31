package io.github.hylexus.jt.jt1078.support.extension.flatform.process.impl;

import io.github.hylexus.jt.jt1078.support.extension.flatform.process.PlatformProcess;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
public class DefaultPlatformProcess implements PlatformProcess, Runnable {
    private final String id;
    private final Process process;
    private final Function<Process, Process> onExitCallback;
    private final String command;
    private final AtomicBoolean running = new AtomicBoolean(false);

    private final ExecutorService executorService;

    public DefaultPlatformProcess(String id, String command, Function<Process, Process> onExitCallback, ExecutorService executorService) {
        this.id = id;
        this.onExitCallback = onExitCallback;
        this.command = command;
        this.executorService = executorService;
        this.process = this.createProcess();
    }

    @Override
    public boolean running() {
        // todo 待确认(JDK8)
        // return this.running.get() && (this.process() != null && this.process().toHandle().isAlive());
        return this.running.get() && (this.process() != null && this.process().isAlive());
    }


    @Override
    public void destroy(Consumer<PlatformProcess> action) {
        if (!this.running.compareAndSet(true, false)) {
            return;
        }
        try {
            if (this.process != null) {
                this.process.destroy();
                log.info("{} closed. Description: {}", this.getClass().getSimpleName(), this);
            }
        } finally {
            action.accept(this);
        }
    }

    @Override
    public void start() {
        this.start(this.executorService);
    }

    @Override
    public void start(ExecutorService executorService) {
        executorService.submit(this);
    }

    @Override
    public void run() {
        if (!this.running.compareAndSet(false, true)) {
            return;
        }
        // todo 待确认(JDK8)
        try {
            // this.process().onExit().thenApply(this.onExitCallback).get();
            final int exitedCode = this.process.waitFor();
            log.info("{} exited with code : {}", this, exitedCode);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            this.onExitCallback.apply(this.process);
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

    public Process process() {
        return process;
    }

    @Override
    public String command() {
        return this.command;
    }

    @Override
    public String uuid() {
        return id;
    }

    @Override
    public String toString() {
        return "DefaultPlatformProcess{"
                + "command='" + command + '\''
                + '}';
    }
}
