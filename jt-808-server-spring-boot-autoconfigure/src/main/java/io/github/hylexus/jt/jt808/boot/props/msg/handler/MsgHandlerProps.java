package io.github.hylexus.jt.jt808.boot.props.msg.handler;

import io.github.hylexus.jt.jt808.spec.impl.DefaultJt808ServerSchedulerFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Duration;

@Getter
@Setter
@ToString
public class MsgHandlerProps {
    private boolean enabled = true;
    private int corePoolSize = 128;
    private int maxPoolSize = 256;
    private Duration keepAlive = Duration.ofMinutes(1);
    private int maxPendingTasks = 256;
    private boolean daemon = true;
    private String poolName = "808-handler";

    public DefaultJt808ServerSchedulerFactory.ExecutorProps toExecutorProps() {
        final DefaultJt808ServerSchedulerFactory.ExecutorProps executorProps = new DefaultJt808ServerSchedulerFactory.ExecutorProps();
        executorProps.setCorePoolSize(corePoolSize);
        executorProps.setMaxPoolSize(maxPoolSize);
        executorProps.setKeepAlive(keepAlive);
        executorProps.setQueueSize(maxPendingTasks);
        executorProps.setDaemon(daemon);
        executorProps.setThreadNamePrefix(poolName);
        return executorProps;
    }
}
