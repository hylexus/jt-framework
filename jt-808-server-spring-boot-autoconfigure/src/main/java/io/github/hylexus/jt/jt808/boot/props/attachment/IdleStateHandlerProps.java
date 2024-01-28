package io.github.hylexus.jt.jt808.boot.props.attachment;

import lombok.Data;

import java.time.Duration;

@Data
public class IdleStateHandlerProps {
    private boolean enabled = true;
    private Duration readerIdleTime = Duration.ofMinutes(20);
    private Duration writerIdleTime = Duration.ZERO;
    private Duration allIdleTime = Duration.ZERO;
}