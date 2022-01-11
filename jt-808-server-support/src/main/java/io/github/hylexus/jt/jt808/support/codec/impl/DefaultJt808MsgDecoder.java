package io.github.hylexus.jt.jt808.support.codec.impl;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.exception.JtIllegalStateException;
import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.*;
import io.github.hylexus.jt.jt808.spec.impl.DefaultJt808MsgBodyProps;
import io.github.hylexus.jt.jt808.spec.impl.DefaultJt808RequestHeader;
import io.github.hylexus.jt.jt808.spec.impl.DefaultJt808SubPackage;
import io.github.hylexus.jt.jt808.spec.impl.request.DefaultJt808Request;
import io.github.hylexus.jt.jt808.spec.impl.request.DefaultJt808SubPackageRequest;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgBytesProcessor;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgDecoder;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.utils.JtProtocolUtils;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * @author hylexus
 */
@Slf4j(topic = "jt-808.request.decoder")
@BuiltinComponent
public class DefaultJt808MsgDecoder implements Jt808MsgDecoder {

    private final Jt808MsgTypeParser msgTypeParser;
    private final Jt808MsgBytesProcessor msgBytesProcessor;
    private final Jt808ProtocolVersionDetectorRegistry versionDetectorRegistry;

    public DefaultJt808MsgDecoder(
            Jt808MsgTypeParser msgTypeParser,
            Jt808MsgBytesProcessor msgBytesProcessor,
            Jt808ProtocolVersionDetectorRegistry versionDetectorRegistry) {

        this.msgTypeParser = msgTypeParser;
        this.msgBytesProcessor = msgBytesProcessor;
        this.versionDetectorRegistry = versionDetectorRegistry;
    }

    @Override
    public Jt808Request decode(ByteBuf byteBuf) {
        if (log.isDebugEnabled()) {
            log.debug("- >>>>>>>>>>>>>>> : 7E{}7E", HexStringUtils.byteBufToString(byteBuf));
        }
        final ByteBuf escaped = this.msgBytesProcessor.doEscapeForReceive(byteBuf);

        if (log.isDebugEnabled()) {
            log.debug("+ >>>>>>>>>>>>>>> : 7E{}7E", HexStringUtils.byteBufToString(escaped));
        }

        final Jt808RequestHeader header = this.parseMsgHeaderSpec(escaped);
        final int msgBodyStartIndex = Jt808RequestHeader.msgBodyStartIndex(header.version(), header.msgBodyProps().hasSubPackage());
        final MsgType msgType = this.parseMsgType(header);
        final byte originalCheckSum = escaped.getByte(escaped.readableBytes() - 1);
        final byte calculatedCheckSum = this.msgBytesProcessor.calculateCheckSum(escaped.slice(0, escaped.readableBytes() - 1));
        // 5. byte[17-20]     消息包封装项
        if (header.msgBodyProps().hasSubPackage()) {
            // byte[0-2)   消息包总数(word(16))
            final int totalSubPackageCountStartIndex = msgBodyStartIndex - 2 * MsgDataType.WORD.getByteCount();
            final int total = JtProtocolUtils.getWord(byteBuf, totalSubPackageCountStartIndex);
            // byte[2-4)   包序号(word(16))
            final int currentNo = JtProtocolUtils.getWord(byteBuf, totalSubPackageCountStartIndex + MsgDataType.WORD.getByteCount());
            final ByteBuf subPackageBody = escaped.slice(msgBodyStartIndex, header.msgBodyLength()).copy();
            final Jt808SubPackageRequest.Jt808SubPackage subPackageSpec = new DefaultJt808SubPackage(
                    header.terminalId(), header.msgId(), header.flowId(),
                    total, currentNo,
                    subPackageBody, LocalDateTime.now()
            );
            final DefaultJt808SubPackageRequest request = new DefaultJt808SubPackageRequest(
                    msgType, header,
                    escaped, escaped.slice(msgBodyStartIndex, header.msgBodyLength()),
                    originalCheckSum, calculatedCheckSum,
                    subPackageSpec
            );
            this.debugLog(msgType, subPackageSpec, request);
            return request;
        } else {
            return new DefaultJt808Request(
                    msgType, header,
                    escaped, escaped.slice(msgBodyStartIndex, header.msgBodyLength()),
                    originalCheckSum, calculatedCheckSum
            );
        }
    }

    private void debugLog(MsgType msgType, DefaultJt808Request request) {
        if (log.isDebugEnabled()) {
            log.debug("+ >>>>>>>>>>>>>>> ({}--{}) {}/{}: 7E{}7E",
                    HexStringUtils.int2HexString(msgType.getMsgId(), 4),
                    request.rawByteBuf().readableBytes() + 2,
                    1,
                    1,
                    HexStringUtils.byteBufToString(request.rawByteBuf())
            );
        }
    }

    private void debugLog(MsgType msgType, Jt808SubPackageRequest.Jt808SubPackage subPackageSpec, DefaultJt808SubPackageRequest request) {
        if (log.isDebugEnabled()) {
            log.debug("+ >>>>>>>>>>>>>>> ({}--{}) {}/{}: 7E{}7E",
                    HexStringUtils.int2HexString(msgType.getMsgId(), 4),
                    request.rawByteBuf().readableBytes() + 2,
                    Math.max(subPackageSpec.currentPackageNo(), 1), Math.max(subPackageSpec.totalSubPackageCount(), 1),
                    HexStringUtils.byteBufToString(request.rawByteBuf())
            );
        }
    }

    private MsgType parseMsgType(Jt808RequestHeader header) {
        final int msgId = header.msgId();
        return this.msgTypeParser.parseMsgType(msgId)
                .orElseThrow(() -> {
                    log.error("Received unknown msg, msgId = {}({}). ignore.", msgId, HexStringUtils.int2HexString(msgId, 4));
                    return new JtIllegalStateException("Received unknown msg, msgId=" + msgId);
                });
    }

    private Jt808RequestHeader parseMsgHeaderSpec(ByteBuf byteBuf) {
        // 1. bytes[0-1] WORD
        final int msgId = JtProtocolUtils.getWord(byteBuf, 0);
        // bytes[2-3] WORD 消息体属性
        final int msgBodyPropsIntValue = JtProtocolUtils.getWord(byteBuf, 2);
        final Jt808RequestHeader.Jt808MsgBodyProps msgBodyProps = new DefaultJt808MsgBodyProps(msgBodyPropsIntValue);

        final Jt808ProtocolVersion version = this.versionDetectorRegistry.getJt808ProtocolVersionDetector(msgId).detectVersion(msgId, msgBodyProps, byteBuf);
        if (version.getVersionBit() == 1) {
            return this.parseHeaderGreatThanOrEqualsV2019(version, msgBodyProps, byteBuf);
        } else if (version == Jt808ProtocolVersion.VERSION_2013 || version == Jt808ProtocolVersion.VERSION_2011) {
            return this.parseHeaderLessThanOrEqualsV2013(version, msgBodyProps, byteBuf);
        }

        throw new JtIllegalStateException("未知版本: " + version);
    }

    private Jt808RequestHeader parseHeaderGreatThanOrEqualsV2019(
            Jt808ProtocolVersion version, Jt808RequestHeader.Jt808MsgBodyProps msgBodyProps, ByteBuf byteBuf) {

        final DefaultJt808RequestHeader headerSpec = new DefaultJt808RequestHeader();
        headerSpec.version(version);
        // 1. bytes[0-1] WORD
        final int msgId = JtProtocolUtils.getWord(byteBuf, 0);
        headerSpec.msgType(msgId);
        // 2. bytes[2-3] WORD
        headerSpec.msgBodyProps(msgBodyProps);

        // 3. bytes[4] WORD 协议版本号
        // final byte version = byteBuf.getByte(4);
        // assert version == 1; // V2019
        // 4. byte[5-14]   终端手机号或设备ID bcd[10]
        headerSpec.terminalId(JtProtocolUtils.getBcd(byteBuf, 5, 10));

        // 4. byte[15-16]     消息流水号
        headerSpec.flowId(JtProtocolUtils.getWord(byteBuf, 15));
        return headerSpec;
    }

    private Jt808RequestHeader parseHeaderLessThanOrEqualsV2013(
            Jt808ProtocolVersion version, Jt808RequestHeader.Jt808MsgBodyProps msgBodyProps, ByteBuf byteBuf) {

        final DefaultJt808RequestHeader headerSpec = new DefaultJt808RequestHeader();
        headerSpec.version(version);
        // 1. bytes[0-1] WORD
        final int msgId = JtProtocolUtils.getWord(byteBuf, 0);
        headerSpec.msgType(msgId);

        // 2. bytes[2-3] WORD
        headerSpec.msgBodyProps(msgBodyProps);

        // 3. byte[4-9]   终端手机号或设备ID bcd[6]
        headerSpec.terminalId(JtProtocolUtils.getBcd(byteBuf, 4, 6));

        // 4. byte[10-11]     消息流水号
        headerSpec.flowId(JtProtocolUtils.getWord(byteBuf, 10));
        return headerSpec;
    }

}
