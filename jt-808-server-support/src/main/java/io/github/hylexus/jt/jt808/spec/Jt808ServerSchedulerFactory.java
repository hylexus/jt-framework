package io.github.hylexus.jt.jt808.spec;

import java.util.concurrent.ExecutorService;

/**
 * @author hylexus
 */
public interface Jt808ServerSchedulerFactory {

    /**
     * @return 业务线程池
     * @since 2.3.0
     */
    ExecutorService getMsgHandlerExecutor();

}
