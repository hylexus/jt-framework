package io.github.hylexus.jt.jt808.support.netty;

import lombok.Data;

import java.time.Duration;

@Data
public class InternalIdleStateHandlerProps {
    private boolean enabled;
    private Duration readerIdleTime;
    private Duration writerIdleTime;
    private Duration allIdleTime;

    public InternalIdleStateHandlerProps(boolean enabled, Duration readerIdleTime, Duration writerIdleTime, Duration allIdleTime) {
        this.enabled = enabled;
        this.readerIdleTime = readerIdleTime;
        this.writerIdleTime = writerIdleTime;
        this.allIdleTime = allIdleTime;
    }
}