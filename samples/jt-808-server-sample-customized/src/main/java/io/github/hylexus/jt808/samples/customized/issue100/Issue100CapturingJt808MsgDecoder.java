package io.github.hylexus.jt808.samples.customized.issue100;

import io.github.hylexus.jt.jt808.spec.Jt808MsgEncryptionHandler;
import io.github.hylexus.jt.jt808.spec.Jt808MsgTypeParser;
import io.github.hylexus.jt.jt808.spec.Jt808ProtocolVersionDetectorRegistry;
import io.github.hylexus.jt.jt808.spec.MutableJt808Request;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgBytesProcessor;
import io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808MsgDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

/**
 * issue-100 示例：在解码器中抓取“线上原始物理帧 + 初步解码后的元数据”。
 *
 * <p>选择这个切入点是因为只有这里同时具备两个条件：</p>
 * <ul>
 *     <li>{@code super.decode(byteBuf)} 之前还能拿到尚未 {@code doEscapeForReceive} 的原始 payload；</li>
 *     <li>{@code super.decode(byteBuf)} 之后已经能拿到 terminalId/msgId/version/flowId/分包号等元数据。</li>
 * </ul>
 *
 * <p>注意：这里只做内存快照和非阻塞投递，不做数据库 IO；否则会拖慢解码/分发线程。</p>
 *
 * @see <a href="https://github.com/hylexus/jt-framework/issues/100">https://github.com/hylexus/jt-framework/issues/100</a>
 */
public class Issue100CapturingJt808MsgDecoder extends DefaultJt808MsgDecoder {
    private static final Logger log = LoggerFactory.getLogger(Issue100CapturingJt808MsgDecoder.class);
    // 本进程内递增的接收序号，用于按服务端接收顺序重放物理帧。
    private static final AtomicLong RECEIVE_SEQ = new AtomicLong();

    private final Issue100TerminalRawPacketCollector rawPacketCollector;

    public Issue100CapturingJt808MsgDecoder(
            Jt808MsgTypeParser msgTypeParser,
            Jt808MsgBytesProcessor msgBytesProcessor,
            Jt808ProtocolVersionDetectorRegistry versionDetectorRegistry,
            Jt808MsgEncryptionHandler encryptionHandler,
            Issue100TerminalRawPacketCollector rawPacketCollector) {
        super(msgTypeParser, msgBytesProcessor, versionDetectorRegistry, encryptionHandler);
        this.rawPacketCollector = rawPacketCollector;
    }

    @Override
    public MutableJt808Request decode(ByteBuf byteBuf) {
        // DelimiterBasedFrameDecoder 已经剥掉首尾 0x7e；这里复制的是尚未 doEscapeForReceive 的线上原始 payload。
        // 必须复制成 byte[]，不能把 ByteBuf 直接交给异步线程：后续 decode 会读/改它，框架最终也会 release。
        final long receiveSeq = RECEIVE_SEQ.incrementAndGet();
        final byte[] wirePayload = ByteBufUtil.getBytes(byteBuf, byteBuf.readerIndex(), byteBuf.readableBytes(), true);
        try {
            final MutableJt808Request request = super.decode(byteBuf);
            // 解码成功后再构造快照，才能把原始物理帧和初步解码出的元数据绑定到一起。
            this.rawPacketCollector.tryPublish(Issue100TerminalRawPacket.decoded(receiveSeq, request, wirePayload));
            return request;
        } catch (Error throwable) {
            // 坏包也保留原始帧，便于离线排查；此时没有 terminalId/msgId 等解码后元数据。
            this.rawPacketCollector.tryPublish(Issue100TerminalRawPacket.decodeFailed(receiveSeq, wirePayload, throwable));
            log.warn("[issue-100] capture decode-failed raw packet: receiveSeq={}", receiveSeq, throwable);
            throw throwable;
        }
    }
}
