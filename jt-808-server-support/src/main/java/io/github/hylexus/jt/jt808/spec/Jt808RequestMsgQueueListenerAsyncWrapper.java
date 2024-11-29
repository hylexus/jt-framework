package io.github.hylexus.jt.jt808.spec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

/**
 * @author hylexus
 */
public class Jt808RequestMsgQueueListenerAsyncWrapper implements Jt808RequestMsgQueueListener {

    private static final Logger log = LoggerFactory.getLogger(Jt808RequestMsgQueueListenerAsyncWrapper.class);
    private final Jt808RequestMsgQueueListener delegate;
    protected final ExecutorService executorService;

    public Jt808RequestMsgQueueListenerAsyncWrapper(Jt808RequestMsgQueueListener delegate, ExecutorService executorService) {
        this.delegate = delegate;
        this.executorService = executorService;
    }

    @Override
    public void consumeMsg(Jt808Request jt808Request) throws Exception {
        // 分包合并逻辑串行执行
        // 因为内置的分包合并器是非线程安全的
        // 如果你自定义了线程安全的分包合并器，可以进行并行分包合并
        if (jt808Request.header().msgBodyProps().hasSubPackage()) {
            this.delegate.consumeMsg(jt808Request);
            return;
        }

        jt808Request.body().retain();
        try {
            executorService.submit(() -> {
                try {
                    this.delegate.consumeMsg(jt808Request);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    jt808Request.body().release();
                }
            });
        } catch (Throwable e) {
            jt808Request.body().release();
            log.error("Error occurred while submit task", e);
            throw new RuntimeException(e);
        }
    }
}
