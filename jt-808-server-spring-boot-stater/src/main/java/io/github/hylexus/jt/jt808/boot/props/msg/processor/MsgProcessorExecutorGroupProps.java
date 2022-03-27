package io.github.hylexus.jt.jt808.boot.props.msg.processor;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

/**
 * @author hylexus
 */
@Getter
@Setter
@ToString
@Slf4j
@Validated
public class MsgProcessorExecutorGroupProps {

    @NotEmpty(message = "poolName is null or empty. defaultValue = '808-msg-processor' ")
    private String poolName = "808-msg-processor";

    /**
     * 默认: {@link Runtime#availableProcessors()} * 2。
     */
    @Min(value = 1, message = "threadCount >= 1, defaultValue = Runtime.getRuntime().availableProcessors() * 2")
    private int threadCount = Runtime.getRuntime().availableProcessors() * 2;

    @Min(value = 16, message = "maxPendingTasks >= 16, defaultValue = 128")
    private int maxPendingTasks = 128;

}
