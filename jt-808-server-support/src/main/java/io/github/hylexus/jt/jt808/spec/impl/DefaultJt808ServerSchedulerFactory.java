package io.github.hylexus.jt.jt808.spec.impl;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.github.hylexus.jt.jt808.spec.Jt808ServerSchedulerFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Duration;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DefaultJt808ServerSchedulerFactory implements Jt808ServerSchedulerFactory {
    private final ExecutorService msgHandlerExecutor;

    public DefaultJt808ServerSchedulerFactory(ExecutorProps executorProps) {
        this.msgHandlerExecutor = this.initMsgHandlerExecutor(executorProps);
    }

    private ExecutorService initMsgHandlerExecutor(ExecutorProps executorProps) {
        return new ThreadPoolExecutor(
                executorProps.getCorePoolSize(),
                executorProps.getMaxPoolSize(),
                executorProps.getKeepAlive().getSeconds(),
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(executorProps.getQueueSize()),
                new ThreadFactoryBuilder()
                        .setNameFormat(executorProps.getThreadNamePrefix() + "-%d")
                        .setDaemon(executorProps.isDaemon())
                        .build()
        );
    }

    @Override
    public ExecutorService getMsgHandlerExecutor() {
        return this.msgHandlerExecutor;
    }

    @Getter
    @Setter
    @ToString
    public static class ExecutorProps {
        private int corePoolSize;
        private int maxPoolSize;
        private Duration keepAlive;
        private int queueSize;
        private boolean daemon = true;
        private String threadNamePrefix;
    }
}
