package io.github.hylexus.jt808.boot.props.dispatcher;

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
 * Created At 2019-08-28 11:50
 */
@Getter
@Setter
@ToString
@Slf4j
@Validated
public class MsgDispatcherThreadPoolProps {

    @Min(value = 1, message = "corePoolSize >= 1, defaultValue = Runtime.getRuntime().availableProcessors() + 1")
    private int corePoolSize = Runtime.getRuntime().availableProcessors() + 1;

    @Min(value = 1, message = "maximumPoolSize >= 1, defaultValue = 2 * corePoolSize")
    private int maximumPoolSize = 2 * corePoolSize;

    private Duration keepAliveTime = Duration.ofSeconds(60);

    @Min(value = 1, message = "blockingQueueSize >= 1, defaultValue = 20")
    private int blockingQueueSize = 20;

    @NotEmpty(message = "threadNameFormat is null or empty. defaultConfig = '808-msg-dispatcher-%d' ")
    private String threadNameFormat = "808-msg-dispatcher-%d";

}
