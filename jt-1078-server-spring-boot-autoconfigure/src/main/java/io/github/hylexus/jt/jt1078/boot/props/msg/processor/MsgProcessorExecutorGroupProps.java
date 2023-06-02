package io.github.hylexus.jt.jt1078.boot.props.msg.processor;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;

/**
 * @author hylexus
 */
@Getter
@Setter
@ToString
@Slf4j
@Validated
public class MsgProcessorExecutorGroupProps {

    private String poolName = "1078-msg-processor";

    /**
     * 默认: 128。
     */
    private int threadCount = 128;

    private int maxPendingTasks = 128;

}
