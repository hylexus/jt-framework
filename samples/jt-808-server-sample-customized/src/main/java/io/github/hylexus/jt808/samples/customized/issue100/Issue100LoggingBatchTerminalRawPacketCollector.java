package io.github.hylexus.jt808.samples.customized.issue100;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 用日志模拟“异步批量写库”。
 *
 * <p>真实项目中把 {@link #mockBatchInsert(List)} 替换为 repository.batchInsert 或 MQ 消费端即可。</p>
 *
 * @see <a href="https://github.com/hylexus/jt-framework/issues/100">https://github.com/hylexus/jt-framework/issues/100</a>
 */
public class Issue100LoggingBatchTerminalRawPacketCollector
        implements Issue100TerminalRawPacketCollector, InitializingBean, DisposableBean {

    private static final Logger log = LoggerFactory.getLogger(Issue100LoggingBatchTerminalRawPacketCollector.class);

    // 有界队列用于隔离解码线程和“写库”线程；队列满时宁可丢弃样例数据，也不要阻塞协议处理链路。
    private final BlockingQueue<Issue100TerminalRawPacket> queue;
    private final int batchSize;
    private final Duration flushInterval;
    // 示例里只用单线程模拟 DB writer；生产环境可以替换成 MQ producer 或真正的批量入库线程池。
    private final ScheduledExecutorService dbWriterExecutor;

    public Issue100LoggingBatchTerminalRawPacketCollector(int queueCapacity, int batchSize, Duration flushInterval) {
        this.queue = new ArrayBlockingQueue<>(queueCapacity);
        this.batchSize = batchSize;
        this.flushInterval = flushInterval;
        this.dbWriterExecutor = Executors.newSingleThreadScheduledExecutor(runnable -> {
            final Thread thread = new Thread(runnable, "issue100-raw-packet-db-writer");
            thread.setDaemon(true);
            return thread;
        });
    }

    @Override
    public void afterPropertiesSet() {
        this.dbWriterExecutor.scheduleWithFixedDelay(
                this::flushSafely,
                this.flushInterval.toMillis(),
                this.flushInterval.toMillis(),
                TimeUnit.MILLISECONDS
        );
        log.info("[issue-100] terminal raw packet collector started: queueCapacity={}, batchSize={}, flushInterval={}",
                this.queue.remainingCapacity(), this.batchSize, this.flushInterval);
    }

    @Override
    public boolean tryPublish(Issue100TerminalRawPacket packet) {
        // offer 是非阻塞的；不要在 decoder 线程里 put/submit 后无限等待。
        final boolean offered = this.queue.offer(packet);
        if (!offered) {
            log.warn("[issue-100] raw packet archive queue is full, discard packet: receiveSeq={}, terminalId={}, msgId=0x{}",
                    packet.getReceiveSeq(), packet.getTerminalId(), toMsgIdHex(packet.getMsgId()));
        }
        return offered;
    }

    @Override
    public void destroy() {
        this.dbWriterExecutor.shutdown();
        this.flushSafely();
        try {
            if (!this.dbWriterExecutor.awaitTermination(3, TimeUnit.SECONDS)) {
                this.dbWriterExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            this.dbWriterExecutor.shutdownNow();
        }
    }

    private void flushSafely() {
        try {
            final List<Issue100TerminalRawPacket> batch = new ArrayList<>(this.batchSize);
            // drainTo 批量搬运，减少锁竞争；一次最多处理 batchSize 条，避免单次 flush 占用太久。
            this.queue.drainTo(batch, this.batchSize);
            if (!batch.isEmpty()) {
                this.mockBatchInsert(batch);
            }
        } catch (Throwable e) {
            log.error("[issue-100] mock batch insert failed", e);
        }
    }

    private void mockBatchInsert(List<Issue100TerminalRawPacket> batch) {
        // todo [issue-100] 真实落库时建议按 batch 执行 insert/update，而不是逐条同步写。
        log.info("[issue-100] mock batch insert terminal raw packets: batchSize={}", batch.size());
        for (Issue100TerminalRawPacket packet : batch) {
            if (packet.isDecodeSuccess()) {
                log.info("[issue-100] INSERT jt808_raw_packet(receiveSeq={}, groupKey={}, terminalId={}, msgId=0x{}, "
                                + "version={}, flowId={}, subPackage={}/{}, checksumOk={}, wirePayloadHex={}, wireFrameLength={}, wireFrameHex={})",
                        packet.getReceiveSeq(),
                        packet.getGroupKey(),
                        packet.getTerminalId(),
                        toMsgIdHex(packet.getMsgId()),
                        packet.getVersion(),
                        packet.getFlowId(),
                        packet.getCurrentPackageNo(),
                        packet.getTotalSubPackageCount(),
                        packet.isChecksumOk(),
                        packet.getWirePayloadHex(),
                        packet.getWireFrameLength(),
                        packet.getWireFrameHex()
                );
            } else {
                log.info("[issue-100] INSERT jt808_raw_packet(receiveSeq={}, decodeSuccess=false, errorType={}, "
                                + "errorMessage={}, wirePayloadHex={}, wireFrameLength={}, wireFrameHex={})",
                        packet.getReceiveSeq(),
                        packet.getErrorType(),
                        packet.getErrorMessage(),
                        packet.getWirePayloadHex(),
                        packet.getWireFrameLength(),
                        packet.getWireFrameHex()
                );
            }
        }
    }

    private static String toMsgIdHex(int msgId) {
        return String.format("%04X", msgId);
    }
}
