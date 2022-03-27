package io.github.hylexus.jt.jt808.boot.props.msg.processor;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.time.Duration;

/**
 * @author hylexus
 */
@Getter
@Setter
@ToString
@Slf4j
@Validated
@Deprecated
public class MsgProcessorThreadPoolProps {

    /**
     * 核心线程数. 默认: {@link Runtime#availableProcessors()} * 2
     */
    @Min(value = 1, message = "corePoolSize >= 1, defaultValue = Runtime.getRuntime().availableProcessors() * 2")
    private int corePoolSize = Runtime.getRuntime().availableProcessors() * 2;

    /**
     * 最大线程数. 默认: {@link #corePoolSize} * 2
     */
    @Deprecated
    @Min(value = 1, message = "maximumPoolSize >= 1, defaultValue = 2 * corePoolSize")
    private int maximumPoolSize = 2 * corePoolSize;

    @Deprecated
    private Duration keepAliveTime = Duration.ofSeconds(60);

    @Min(value = 1, message = "blockingQueueSize >= 1, defaultValue = 100")
    private int blockingQueueSize = 128;

    @NotEmpty(message = "threadNameFormat is null or empty. defaultConfig = '808-msg-dispatcher-%d' ")
    private String threadNameFormat = "808-msg-processor";

}
