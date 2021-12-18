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

/**
 * @author hylexus
 */
@Slf4j(topic = "jt-808.request.decoder")
@BuiltinComponent
public class DefaultJt808MsgDecoder implements Jt808MsgDecoder {

    private final Jt808MsgTypeParser msgTypeParser;
    private final Jt808MsgBytesProcessor msgBytesProcessor;

    public DefaultJt808MsgDecoder(Jt808MsgTypeParser msgTypeParser, Jt808MsgBytesProcessor msgBytesProcessor) {
        this.msgTypeParser = msgTypeParser;
        this.msgBytesProcessor = msgBytesProcessor;
    }

    @Override
    public Jt808Request decode(Jt808ProtocolVersion version, ByteBuf byteBuf) {
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
                    header.terminalId(), header.msgId(),
                    total, currentNo,
                    subPackageBody
            );

            return new DefaultJt808SubPackageRequest(
                    msgType, header,
                    escaped, escaped.slice(msgBodyStartIndex, header.msgBodyLength()),
                    originalCheckSum, calculatedCheckSum, subPackageSpec
            );
        } else {
            return new DefaultJt808Request(
                    msgType, header,
                    escaped, escaped.slice(msgBodyStartIndex, header.msgBodyLength()),
                    originalCheckSum, calculatedCheckSum
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
        // bytes[2-3] WORD 消息体属性
        final int msgBodyPropsIntValue = JtProtocolUtils.getWord(byteBuf, 2);
        final DefaultJt808MsgBodyProps msgBodyProps = new DefaultJt808MsgBodyProps(msgBodyPropsIntValue);

        // 消息体中版本标识为1
        if (msgBodyProps.versionIdentifier() == 1) {
            // bytes[4] Byte
            final byte version = byteBuf.getByte(4);
            if (version == Jt808ProtocolVersion.VERSION_2019.getVersionBit()) {
                return this.parseHeaderV2019(msgBodyProps, byteBuf);
            } else if (version == Jt808ProtocolVersion.VERSION_2011.getVersionBit()) {
                return this.parseHeaderV2011(msgBodyProps, byteBuf);
            } else {
                throw new JtIllegalStateException("未知版本: " + version);
            }
        } else {
            return this.parseHeaderV2011(msgBodyProps, byteBuf);
        }
    }

    private Jt808RequestHeader parseHeaderV2019(Jt808RequestHeader.Jt808MsgBodyProps msgBodyProps, ByteBuf byteBuf) {
        final DefaultJt808RequestHeader headerSpec = new DefaultJt808RequestHeader();
        headerSpec.version(Jt808ProtocolVersion.VERSION_2019);
        // 1. bytes[0-1] WORD
        final int msgId = JtProtocolUtils.getWord(byteBuf, 0);
        headerSpec.msgType(msgId);
        // 2. bytes[2-3] WORD
        headerSpec.msgBodyProps(msgBodyProps);

        // 3. bytes[4] WORD 协议版本号
        final byte version = byteBuf.getByte(4);
        assert version == 1;
        // 4. byte[5-14]   终端手机号或设备ID bcd[10]
        headerSpec.terminalId(JtProtocolUtils.getBcd(byteBuf, 5, 10));

        // 4. byte[15-16]     消息流水号
        headerSpec.flowId(JtProtocolUtils.getWord(byteBuf, 15));
        return headerSpec;
    }

    private Jt808RequestHeader parseHeaderV2011(Jt808RequestHeader.Jt808MsgBodyProps msgBodyProps, ByteBuf byteBuf) {
        final DefaultJt808RequestHeader headerSpec = new DefaultJt808RequestHeader();
        headerSpec.version(Jt808ProtocolVersion.VERSION_2011);
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
