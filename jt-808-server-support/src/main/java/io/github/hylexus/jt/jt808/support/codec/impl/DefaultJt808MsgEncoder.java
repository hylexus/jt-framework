package io.github.hylexus.jt.jt808.support.codec.impl;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.config.JtProtocolConstant;
import io.github.hylexus.jt.exception.JtIllegalStateException;
import io.github.hylexus.jt.jt808.response.Jt808Response;
import io.github.hylexus.jt.jt808.spec.Jt808MsgHeaderSpec;
import io.github.hylexus.jt.jt808.support.codec.Jt808ByteBuf;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgBytesProcessor;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgEncoder;
import io.github.hylexus.jt.utils.JtProtocolUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

/**
 * @author hylexus
 */
public class DefaultJt808MsgEncoder implements Jt808MsgEncoder {

    private final ByteBufAllocator allocator = PooledByteBufAllocator.DEFAULT;

    private final Jt808MsgBytesProcessor msgBytesProcessor;

    public DefaultJt808MsgEncoder(Jt808MsgBytesProcessor msgBytesProcessor) {
        this.msgBytesProcessor = msgBytesProcessor;
    }

    @Override
    public ByteBuf encode(Jt808Response response) {
        final Jt808ByteBuf headerBuf = encodeMsgHeader(response);
        final Jt808ByteBuf bodyBuf = response.body().data();

        final CompositeByteBuf compositeByteBuf = allocator.compositeBuffer()
                .addComponent(true, headerBuf)
                .addComponent(true, bodyBuf);

        final byte checkSum = this.msgBytesProcessor.calculateCheckSum(compositeByteBuf);

        compositeByteBuf.writeByte(checkSum);
        compositeByteBuf.resetReaderIndex();
        final ByteBuf escaped = this.msgBytesProcessor.doEscapeForSend(compositeByteBuf);

        return allocator.compositeBuffer()
                .addComponent(true, allocator.buffer().writeByte(JtProtocolConstant.PACKAGE_DELIMITER))
                .addComponent(true, escaped)
                .addComponent(true, allocator.buffer().writeByte(JtProtocolConstant.PACKAGE_DELIMITER));
    }

//    @Override
//    public ByteBuf encode(Jt808Response response) {
//        final Jt808ByteBuf headerBuf = encodeMsgHeader(response);
//        final Jt808ByteBuf bodyBuf = response.body().data();
//
//        final CompositeByteBuf compositeByteBuf = allocator.compositeBuffer()
//                .addComponent(true, headerBuf)
//                .addComponent(true, bodyBuf);
//
//        final byte checkSum = this.msgBytesProcessor.calculateCheckSum(compositeByteBuf);
//
//        compositeByteBuf.writeByte(checkSum);
//        compositeByteBuf.resetReaderIndex();
//        final ByteBuf escaped = this.msgBytesProcessor.doEscapeForSend(compositeByteBuf);
//
//        return allocator.compositeBuffer()
//                .addComponent(true, allocator.buffer().writeByte(JtProtocolConstant.PACKAGE_DELIMITER))
//                .addComponent(true, escaped)
//                .addComponent(true, allocator.buffer().writeByte(JtProtocolConstant.PACKAGE_DELIMITER));
//    }

    protected Jt808ByteBuf encodeMsgHeader(Jt808Response response) {
        final Jt808ProtocolVersion version = response.version();
        if (version == Jt808ProtocolVersion.VERSION_2019) {
            return encodeMsgHeaderV2019(response);
        }
        if (version == Jt808ProtocolVersion.VERSION_2011) {
            return encodeMsgHeaderV2011(response);
        }
        throw new JtIllegalStateException("Unsupported version : " + version);
    }

    private Jt808ByteBuf encodeMsgHeaderV2019(Jt808Response response) {
        final int msgBodyStartIndex = Jt808MsgHeaderSpec.msgBodyStartIndex(response.version(), false);
        final Jt808ByteBuf buf = new Jt808ByteBuf(allocator.buffer(msgBodyStartIndex));

        // bytes[0-1] 消息ID Word
        buf.writeWord(response.msgId());

        // bytes[2-3] 消息体属性 Word
        final int bodyPropsForJt808 = JtProtocolUtils.generateMsgBodyPropsForJt808(
                response.msgBodyLength(), response.encryptionType(), false, response.version(), response.reversedBit15InHeader());
        buf.writeWord(bodyPropsForJt808);

        // bytes[4] 协议版本号 byte
        buf.writeByte(response.version().getVersionBit());

        // bytes[5-14] 终端手机号 BCD[6]
        buf.writeBcd(response.terminalId());

        // bytes[15-16] 消息流水号  Word
        buf.writeWord(response.flowId());

        // bytes[17-20] 消息包封装项
        //        if (msgBodyPropsSpec.hasSubPackage()) {
        //            final Jt808MsgHeaderSpec.SubPackageSpec subPackageSpec = header.subPackageSpec()
        //                    .orElseThrow(() -> new JtIllegalStateException("(v2019) msgBodyProps.hasSubPackage() == true, but header.subPackageSpec() is EMPTY."));
        //            // 消息总包包数
        //            buf.writeWord(subPackageSpec.totalSubPackageCount());
        //            // 包序号
        //            buf.writeWord(subPackageSpec.currentPackageNo());
        //        }

        return buf;
    }

    private Jt808ByteBuf encodeMsgHeaderV2011(Jt808Response response) {
        final int msgBodyStartIndex = Jt808MsgHeaderSpec.msgBodyStartIndex(response.version(), false);
        final Jt808ByteBuf buf = new Jt808ByteBuf(allocator.buffer(msgBodyStartIndex));

        // bytes[0-1] 消息ID Word
        buf.writeWord(response.msgId());

        // bytes[2-3] 消息体属性 Word
        final int bodyPropsForJt808 = JtProtocolUtils.generateMsgBodyPropsForJt808(
                response.msgBodyLength(), response.encryptionType(), false, response.version(), response.reversedBit15InHeader());
        buf.writeWord(bodyPropsForJt808);

        // bytes[4-9] 终端手机号 BCD[6]
        buf.writeBcd(response.terminalId());

        // bytes[10-11] 消息流水号  Word
        buf.writeWord(response.flowId());

        // bytes[12-15] 消息包封装项
        //        if (msgBodyPropsSpec.hasSubPackage()) {
        //            final Jt808MsgHeaderSpec.SubPackageSpec subPackageSpec = header.subPackageSpec()
        //                    .orElseThrow(() -> new JtIllegalStateException("(v2011) msgBodyProps.hasSubPackage() == true, but header.subPackageSpec() is EMPTY."));
        //            // 消息总包包数
        //            buf.writeWord(subPackageSpec.totalSubPackageCount());
        //            // 包序号
        //            buf.writeWord(subPackageSpec.currentPackageNo());
        //        }

        return buf;
    }
}
