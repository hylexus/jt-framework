package io.github.hylexus.jt.jt808.support.codec.impl;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.JtProtocolConstant;
import io.github.hylexus.jt.jt808.spec.Jt808RequestHeader;
import io.github.hylexus.jt.jt808.spec.Jt808Response;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgBytesProcessor;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgEncoder;
import io.github.hylexus.jt.jt808.support.utils.JtProtocolUtils;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hylexus
 */
@Slf4j(topic = "jt-808.response.encoder")
@BuiltinComponent
public class DefaultJt808MsgEncoder implements Jt808MsgEncoder {

    private final ByteBufAllocator allocator = PooledByteBufAllocator.DEFAULT;

    private final Jt808MsgBytesProcessor msgBytesProcessor;

    public DefaultJt808MsgEncoder(Jt808MsgBytesProcessor msgBytesProcessor) {
        this.msgBytesProcessor = msgBytesProcessor;
    }

    @Override
    public ByteBuf encode(Jt808Response response) {
        final int maxPackageSize = response.maxPackageSize();
        final int msgBodyLength = response.msgBodyLength();
        final int estimatedPackageSize = Jt808RequestHeader.msgBodyStartIndex(response.version(), false) + msgBodyLength + 3;

        if (estimatedPackageSize <= maxPackageSize) {
            return this.buildPackage(response, response.body(), 0, 0);
        }

        final int subPackageBodySize = maxPackageSize - Jt808RequestHeader.msgBodyStartIndex(response.version(), true) - 3;
        final int subPackageCount = msgBodyLength % subPackageBodySize == 0
                ? msgBodyLength / subPackageBodySize
                : msgBodyLength / subPackageBodySize + 1;

        final CompositeByteBuf allResponseBytes = allocator.compositeBuffer(subPackageCount);
        final ByteBuf body = response.body();
        for (int i = 0; i < subPackageCount; i++) {
            final int offset = i * subPackageBodySize;
            final int length = (i == subPackageCount - 1)
                    ? Math.min(subPackageBodySize, msgBodyLength - offset)
                    : subPackageBodySize;
            final ByteBuf bodyData = body.retainedSlice(offset, length);
            final CompositeByteBuf subPackage = this.buildPackage(response, bodyData, subPackageCount, i + 1);
            allResponseBytes.addComponents(true, subPackage);
        }
        return allResponseBytes;
    }

    private CompositeByteBuf buildPackage(Jt808Response response, ByteBuf body, int totalSubPackageCount, int currentPackageNo) {
        final ByteBuf headerBuf = this.encodeMsgHeader(response, body, totalSubPackageCount > 0, totalSubPackageCount, currentPackageNo);
        final CompositeByteBuf compositeByteBuf = allocator.compositeBuffer()
                .addComponent(true, headerBuf)
                .addComponent(true, body);

        final byte checkSum = this.msgBytesProcessor.calculateCheckSum(compositeByteBuf);

        compositeByteBuf.writeByte(checkSum);
        compositeByteBuf.resetReaderIndex();
        if (log.isDebugEnabled()) {
            log.debug("- <<<<<<<<<<<<<<< ({}--{}) {}/{}: 7E{}7E",
                    HexStringUtils.int2HexString(response.msgType(), 4),
                    compositeByteBuf.readableBytes() + 2,
                    Math.max(currentPackageNo, 1), Math.max(totalSubPackageCount, 1),
                    HexStringUtils.byteBufToString(compositeByteBuf)
            );
        }

        final ByteBuf escaped = this.msgBytesProcessor.doEscapeForSend(compositeByteBuf);

        if (log.isDebugEnabled()) {
            log.debug("+ <<<<<<<<<<<<<<< ({}--{}) {}/{}: 7E{}7E",
                    HexStringUtils.int2HexString(response.msgType(), 4),
                    escaped.readableBytes() + 2,
                    Math.max(currentPackageNo, 1), Math.max(totalSubPackageCount, 1),
                    HexStringUtils.byteBufToString(escaped)
            );
        }
        return allocator.compositeBuffer()
                .addComponent(true, allocator.buffer().writeByte(JtProtocolConstant.PACKAGE_DELIMITER))
                .addComponent(true, escaped)
                .addComponent(true, allocator.buffer().writeByte(JtProtocolConstant.PACKAGE_DELIMITER));
    }

    private ByteBuf encodeMsgHeader(Jt808Response response, ByteBuf body, boolean hasSubPackage, int totalSubPackageCount, int currentSubPackageNo) {
        final Jt808ProtocolVersion version = response.version();
        final ByteBuf header = allocator.buffer();
        // bytes[0-2) 消息ID Word
        JtProtocolUtils.writeWord(header, response.msgType());

        // bytes[2-4) 消息体属性 Word
        final int bodyPropsForJt808 = JtProtocolUtils.generateMsgBodyPropsForJt808(
                body.readableBytes(), 0,
                hasSubPackage, response.version(), response.reversedBit15InHeader()
        );
        JtProtocolUtils.writeWord(header, bodyPropsForJt808);

        // bytes[4] 协议版本号 byte
        if (version == Jt808ProtocolVersion.VERSION_2019) {
            header.writeByte(response.version().getVersionBit());
        }

        // bytes[5-14) 终端手机号 BCD[10]
        // bytes[4-10) 终端手机号 BCD[6]
        JtProtocolUtils.writeBcd(header, response.terminalId());

        // bytes[15-17) 消息流水号  Word
        // TODO bytes[10-12) 消息流水号  Word
        JtProtocolUtils.writeWord(header, response.flowId());

        // bytes[17-21) 消息包封装项
        // bytes[12-16) 消息包封装项
        if (hasSubPackage) {
            // 消息总包包数
            JtProtocolUtils.writeWord(header, totalSubPackageCount);
            // 包序号
            JtProtocolUtils.writeWord(header, currentSubPackageNo);
        }
        return header;
    }

}
