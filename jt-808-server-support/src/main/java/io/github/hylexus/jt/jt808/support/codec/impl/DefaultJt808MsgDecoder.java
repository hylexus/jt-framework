package io.github.hylexus.jt.jt808.support.codec.impl;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt.exception.JtIllegalStateException;
import io.github.hylexus.jt.jt808.request.Jt808Request;
import io.github.hylexus.jt.jt808.request.impl.DefaultJt808Request;
import io.github.hylexus.jt.jt808.spec.Jt808MsgHeader;
import io.github.hylexus.jt.jt808.spec.impl.DefaultJt808MsgHeader;
import io.github.hylexus.jt.jt808.spec.impl.DefaultMsgBodyPropsSpec;
import io.github.hylexus.jt.jt808.spec.impl.DefaultSubPackageSpec;
import io.github.hylexus.jt.jt808.support.MsgTypeParser;
import io.github.hylexus.jt.jt808.support.codec.Jt808ByteBuf;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgBytesProcessor;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgDecoder;
import io.github.hylexus.jt.utils.HexStringUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hylexus
 */
@Slf4j(topic = "jt-808.request.decoder")
@BuiltinComponent
public class DefaultJt808MsgDecoder implements Jt808MsgDecoder {

    private final MsgTypeParser msgTypeParser;
    private final Jt808MsgBytesProcessor msgBytesProcessor;

    public DefaultJt808MsgDecoder(MsgTypeParser msgTypeParser, Jt808MsgBytesProcessor msgBytesProcessor) {
        this.msgTypeParser = msgTypeParser;
        this.msgBytesProcessor = msgBytesProcessor;
    }

    @Override
    public Jt808Request decode(Jt808ProtocolVersion version, Jt808ByteBuf byteBuf) {
        if (log.isDebugEnabled()) {
            log.debug("before escaped: {}", HexStringUtils.byteBufToString(byteBuf));
        }
        final Jt808ByteBuf escaped = Jt808ByteBuf.from(this.msgBytesProcessor.doEscapeForReceive(byteBuf));

        if (log.isDebugEnabled()) {
            log.debug("after escaped: {}", HexStringUtils.byteBufToString(escaped));
        }

        final Jt808MsgHeader headerSpec = this.parseMsgHeaderSpec(version, escaped);
        final byte originalCheckSum = escaped.getByte(escaped.readableBytes() - 1);
        final byte calculatedCheckSum = this.msgBytesProcessor.calculateCheckSum(escaped.slice(0, escaped.readableBytes() - 1));
        final MsgType msgType = this.parseMsgType(headerSpec);

        return new DefaultJt808Request(headerSpec, escaped, originalCheckSum, calculatedCheckSum, msgType);
    }

    private MsgType parseMsgType(Jt808MsgHeader headerSpec) {
        final int msgId = headerSpec.msgType();
        return this.msgTypeParser.parseMsgType(msgId)
                .orElseThrow(() -> {
                    log.error("Received unknown msg, msgId = {}({}). ignore.", msgId, HexStringUtils.int2HexString(msgId, 4));
                    return new JtIllegalStateException("Received unknown msg, msgId=" + msgId);
                });
    }

    /**
     * @param serverSupportedVersion 服务端配置文件中配置的期望终端使用的协议版本，可忽略该参数
     */
    private Jt808MsgHeader parseMsgHeaderSpec(Jt808ProtocolVersion serverSupportedVersion, Jt808ByteBuf byteBuf) {
        // bytes[2-3] WORD 消息体属性
        final int msgBodyPropsIntValue = byteBuf.getWord(2);
        final DefaultMsgBodyPropsSpec msgBodyProps = new DefaultMsgBodyPropsSpec(msgBodyPropsIntValue);

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

    private Jt808MsgHeader parseHeaderV2019(Jt808MsgHeader.MsgBodyPropsSpec msgBodyPropsSpec, Jt808ByteBuf byteBuf) {
        final DefaultJt808MsgHeader headerSpec = new DefaultJt808MsgHeader();
        headerSpec.setVersion(Jt808ProtocolVersion.VERSION_2019);
        // 1. bytes[0-1] WORD
        final int msgId = byteBuf.getWord(0);
        headerSpec.setMsgId(msgId);
        // 2. bytes[2-3] WORD
        headerSpec.setMsgBodyPropsSpec(msgBodyPropsSpec);

        // 3. bytes[4] WORD 协议版本号
        final byte version = byteBuf.getByte(4);
        assert version == 1;
        // 4. byte[5-14]   终端手机号或设备ID bcd[10]
        headerSpec.setTerminalId(byteBuf.getBcd(5, 10));

        // 4. byte[15-16]     消息流水号
        headerSpec.setFlowId(byteBuf.getWord(15));
        // 5. byte[17-20]     消息包封装项
        if (msgBodyPropsSpec.hasSubPackage()) {
            // byte[0-1]   消息包总数(word(16))
            final int total = byteBuf.getWord(17);
            // byte[2-3]   包序号(word(16))
            final int currentNo = byteBuf.getWord(19);
            final DefaultSubPackageSpec subPackageSpec = new DefaultSubPackageSpec(total, currentNo);
            headerSpec.setSubPackageSpec(subPackageSpec);
        }

        return headerSpec;
    }

    private Jt808MsgHeader parseHeaderV2011(Jt808MsgHeader.MsgBodyPropsSpec msgBodyPropsSpec, Jt808ByteBuf byteBuf) {
        final DefaultJt808MsgHeader headerSpec = new DefaultJt808MsgHeader();
        headerSpec.setVersion(Jt808ProtocolVersion.VERSION_2011);
        // 1. bytes[0-1] WORD
        final int msgId = byteBuf.getWord(0);
        headerSpec.setMsgId(msgId);

        // 2. bytes[2-3] WORD
        headerSpec.setMsgBodyPropsSpec(msgBodyPropsSpec);

        // 3. byte[4-9]   终端手机号或设备ID bcd[6]
        headerSpec.setTerminalId(byteBuf.getBcd(4, 6));

        // 4. byte[10-11]     消息流水号
        headerSpec.setFlowId(byteBuf.getWord(10));

        // 5. byte[12-15]     消息包封装项
        if (msgBodyPropsSpec.hasSubPackage()) {
            // byte[0-1]   消息包总数(word(16))
            final int total = byteBuf.getWord(12);
            // byte[2-3]   包序号(word(16))
            final int currentNo = byteBuf.getWord(14);
            final DefaultSubPackageSpec subPackageSpec = new DefaultSubPackageSpec(total, currentNo);
            headerSpec.setSubPackageSpec(subPackageSpec);
        }

        return headerSpec;
    }

}
