package io.github.hylexus.jt.jt808.support.codec.impl;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.exception.JtIllegalStateException;
import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.*;
import io.github.hylexus.jt.jt808.spec.impl.DefaultJt808MsgBodyProps;
import io.github.hylexus.jt.jt808.spec.impl.DefaultJt808RequestHeader;
import io.github.hylexus.jt.jt808.spec.impl.DefaultJt808SubPackageProps;
import io.github.hylexus.jt.jt808.spec.impl.request.DefaultJt808Request;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgBytesProcessor;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgDecoder;
import io.github.hylexus.jt.jt808.support.exception.Jt808UnknownMsgException;
import io.github.hylexus.jt.jt808.support.utils.JtProtocolUtils;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;

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
    public MutableJt808Request decode(ByteBuf byteBuf) {
        if (log.isDebugEnabled()) {
            log.debug("- >>>>>>>>>>>>>>> : 7E{}7E", HexStringUtils.byteBufToString(byteBuf));
        }
        final ByteBuf escaped = this.msgBytesProcessor.doEscapeForReceive(byteBuf);

        if (log.isDebugEnabled()) {
            log.debug("+ >>>>>>>>>>>>>>> : 7E{}7E", HexStringUtils.byteBufToString(escaped));
        }

        final Jt808RequestHeader header = this.parseMsgHeaderSpec(escaped);
        final int msgBodyStartIndex = Jt808RequestHeader.msgBodyStartIndex(header.version(), header.msgBodyProps().hasSubPackage());

        // @see https://github.com/hylexus/jt-framework/issues/78
        final MsgType msgType = this.msgTypeParser.parseMsgType(header.msgId())
                .orElseThrow(() -> {
                    JtProtocolUtils.release(escaped);
                    return new Jt808UnknownMsgException(header.msgId(), byteBuf);
                });

        final byte originalCheckSum = escaped.getByte(escaped.readableBytes() - 1);
        final byte calculatedCheckSum = this.msgBytesProcessor.calculateCheckSum(escaped.slice(0, escaped.readableBytes() - 1));
        final DefaultJt808Request request = new DefaultJt808Request(
                msgType, header,
                escaped, escaped.retainedSlice(msgBodyStartIndex, header.msgBodyLength()),
                originalCheckSum, calculatedCheckSum
        );
        debugLog(msgType, request);
        return request;
    }

    private void debugLog(MsgType msgType, DefaultJt808Request request) {
        if (log.isDebugEnabled()) {
            if (request.header().msgBodyProps().hasSubPackage()) {
                log.debug("+ >>>>>>>>>>>>>>> ({}--{}) {}/{}: 7E{}7E",
                        HexStringUtils.int2HexString(msgType.getMsgId(), 4),
                        request.rawByteBuf().readableBytes() + 2,
                        request.header().subPackage().currentPackageNo(),
                        request.header().subPackage().totalSubPackageCount(),
                        HexStringUtils.byteBufToString(request.rawByteBuf())
                );
            } else {
                log.debug("+ >>>>>>>>>>>>>>> ({}--{}) {}/{}: 7E{}7E",
                        HexStringUtils.int2HexString(msgType.getMsgId(), 4),
                        request.rawByteBuf().readableBytes() + 2,
                        1,
                        1,
                        HexStringUtils.byteBufToString(request.rawByteBuf())
                );
            }
        }
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
        if (msgBodyProps.hasSubPackage()) {
            final DefaultJt808SubPackageProps subPackageProps = new DefaultJt808SubPackageProps();
            subPackageProps.totalSubPackageCount(JtProtocolUtils.getWord(byteBuf, 17));
            subPackageProps.currentPackageNo(JtProtocolUtils.getWord(byteBuf, 19));
            headerSpec.subPackageProps(subPackageProps);
        }
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
        if (msgBodyProps.hasSubPackage()) {
            final DefaultJt808SubPackageProps subPackageProps = new DefaultJt808SubPackageProps();
            subPackageProps.totalSubPackageCount(JtProtocolUtils.getWord(byteBuf, 12));
            subPackageProps.currentPackageNo(JtProtocolUtils.getWord(byteBuf, 14));
            headerSpec.subPackageProps(subPackageProps);
        }
        return headerSpec;
    }

}
