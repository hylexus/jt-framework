package io.github.hylexus.jt808.samples.customized.issue100;

import io.github.hylexus.jt.jt808.spec.Jt808Request;
import io.github.hylexus.jt.utils.FormatUtils;
import lombok.Builder;
import lombok.Getter;
import org.jspecify.annotations.Nullable;

import java.time.Instant;

/**
 * 终端原始物理帧快照。
 *
 * <p>{@code wirePayloadHex} 不包含首尾 {@code 0x7e}，适合转换成 bytes 后重新交给 {@code Jt808MsgDecoder#decode}；
 * {@code wireFrameHex} 包含首尾 {@code 0x7e}，适合转换成 bytes 后按 TCP 收包形态重放。</p>
 *
 * @see <a href="https://github.com/hylexus/jt-framework/issues/100">https://github.com/hylexus/jt-framework/issues/100</a>
 */
@Getter
@Builder
public class Issue100TerminalRawPacket {
    // 本进程内的物理帧接收序号；重放时可以用它恢复服务端接收顺序。
    private final long receiveSeq;
    private final Instant receivedAt;
    private final boolean decodeSuccess;

    // 以下字段来自 super.decode(...) 之后的初步协议头解析；解码失败时这些字段可能为空或为默认值。
    private final String terminalId;
    private final int msgId;
    private final String version;
    private final int flowId;
    private final boolean checksumOk;

    // 分包元数据用于把多个原始物理帧关联成同一个逻辑请求。
    private final boolean hasSubPackage;
    private final Integer totalSubPackageCount;
    private final Integer currentPackageNo;
    private final String groupKey;

    // wirePayloadHex 是不含 0x7e 的原始 payload；wireFrameHex 补回首尾 0x7e，便于按 TCP 帧重放。
    // sample 里用 hex 更利于日志/SQL 查看；如果生产库使用 BLOB，也可以把这里换回 byte[]。
    private final String wirePayloadHex;
    private final String wireFrameHex;

    // 解码失败时保留异常摘要，方便后续按原始帧排查坏包。
    private final String errorType;
    private final String errorMessage;

    public static Issue100TerminalRawPacket decoded(long receiveSeq, Jt808Request request, byte[] wirePayload) {
        final boolean hasSubPackage = request.header().msgBodyProps().hasSubPackage();
        final Integer totalSubPackageCount = hasSubPackage ? request.header().subPackage().totalSubPackageCount() : 1;
        final Integer currentPackageNo = hasSubPackage ? request.header().subPackage().currentPackageNo() : 1;
        final int msgId = request.msgType().getMsgId();
        // 单包按 flowId 归组；分包按 terminalId + msgId + totalSubPackageCount 归组，与当前内置分包缓存策略保持一致。
        final String groupKey = buildGroupKey(request.terminalId(), msgId, request.flowId(), hasSubPackage, totalSubPackageCount);

        return Issue100TerminalRawPacket.builder()
                .receiveSeq(receiveSeq)
                .receivedAt(Instant.now())
                .decodeSuccess(true)
                .terminalId(request.terminalId())
                .msgId(msgId)
                .version(request.version().name())
                .flowId(request.flowId())
                .checksumOk(request.originalCheckSum() == request.calculatedCheckSum())
                .hasSubPackage(hasSubPackage)
                .totalSubPackageCount(totalSubPackageCount)
                .currentPackageNo(currentPackageNo)
                .groupKey(groupKey)
                .wirePayloadHex(toWirePayloadHex(wirePayload))
                .wireFrameHex(toWireFrameHex(wirePayload))
                .build();
    }

    public static Issue100TerminalRawPacket decodeFailed(long receiveSeq, byte[] wirePayload, Throwable throwable) {
        return Issue100TerminalRawPacket.builder()
                .receiveSeq(receiveSeq)
                .receivedAt(Instant.now())
                .decodeSuccess(false)
                .wirePayloadHex(toWirePayloadHex(wirePayload))
                .wireFrameHex(toWireFrameHex(wirePayload))
                .errorType(throwable.getClass().getName())
                .errorMessage(throwable.getMessage())
                .build();
    }

    private static String buildGroupKey(String terminalId, int msgId, int flowId, boolean hasSubPackage, @Nullable Integer totalSubPackageCount) {
        if (hasSubPackage) {
            return terminalId + "_" + int2Hex(msgId) + "_" + totalSubPackageCount;
        }
        return terminalId + "_" + int2Hex(msgId) + "_" + flowId;
    }

    private static String int2Hex(int value) {
        return String.format("0x%04X", value);
    }

    private static String toWirePayloadHex(byte @Nullable [] wirePayload) {
        return wirePayload == null ? "" : FormatUtils.toHexString(wirePayload);
    }

    private static String toWireFrameHex(byte @Nullable [] wirePayload) {
        // Netty 的 DelimiterBasedFrameDecoder 交给 decoder 的内容不带 0x7e，这里补回来用于真实帧重放。
        return "7E" + toWirePayloadHex(wirePayload) + "7E";
    }

    @SuppressWarnings("unused")
    public int getWirePayloadLength() {
        return this.wirePayloadHex.length() / 2;
    }

    public int getWireFrameLength() {
        return this.wireFrameHex.length() / 2;
    }

}
