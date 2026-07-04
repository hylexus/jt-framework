package io.github.hylexus.jt808.samples.customized.issue100;

/**
 * issue-100: 原始终端物理帧采集入口。
 *
 * <p>实现类必须避免阻塞解码线程。真实项目中这里可以替换成 Kafka/RocketMQ/Redis Stream/本地 WAL。</p>
 *
 * @see <a href="https://github.com/hylexus/jt-framework/issues/100">https://github.com/hylexus/jt-framework/issues/100</a>
 */
public interface Issue100TerminalRawPacketCollector {

    /**
     * 尝试投递报文快照。该方法会在 decoder 线程里被调用，必须快速返回。
     *
     * @return {@code true} 表示成功进入异步队列；{@code false} 表示队列已满，本次报文被丢弃。
     */
    boolean tryPublish(Issue100TerminalRawPacket packet);

}
