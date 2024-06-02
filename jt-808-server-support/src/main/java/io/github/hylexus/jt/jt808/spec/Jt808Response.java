package io.github.hylexus.jt.jt808.spec;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.impl.response.DefaultJt808ResponseBuilder;
import io.github.hylexus.jt.jt808.spec.impl.response.DefaultJt808ResponseSubPackage;
import io.github.hylexus.jt.jt808.support.annotation.msg.resp.Jt808ResponseBody;
import io.github.hylexus.jt.jt808.support.codec.Jt808ByteWriter;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.result.Jt808ResponseHandlerResultHandler;
import io.github.hylexus.jt.jt808.support.utils.JtProtocolUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.function.Consumer;

/**
 * @author hylexus
 * @see Jt808ResponseHandlerResultHandler
 */
@BuiltinComponent
public interface Jt808Response extends Jt808ByteWriter {

    int DEFAULT_MAX_PACKAGE_SIZE = 1024;
    int DEFAULT_ENCRYPTION_TYPE = 0b000;

    default ByteBufAllocator allocator() {
        return ByteBufAllocator.DEFAULT;
    }

    /**
     * byte[0,2) -- {@link  MsgDataType#WORD WORD} -- 消息ID
     */
    int msgId();

    Jt808Response msgId(int msgId);

    default Jt808Response msgId(MsgType msgType) {
        return this.msgId(msgType.getMsgId());
    }

    Jt808ProtocolVersion version();

    /**
     * byte[2,4).bit[0,9) -- 消息体长度
     * <p>
     * byte[2,4).bit[13] -- {@link #maxPackageSize()}
     * <p>
     * byte[2,4).bit[14] -- {@link #msgId()}
     */
    default int msgBodyLength() {
        return body().readableBytes();
    }

    /**
     * byte[2,4).bit[10,12) -- 数据加密方式
     *
     * @since 2.1.4
     */
    int encryptionType();

    /**
     * @since 2.1.4
     */
    Jt808Response encryptionType(int encType);

    /**
     * @since 2.1.4
     */
    default Jt808Response encryptionType(Jt808MsgEncryptionType encType) {
        return this.encryptionType(encType.intValue());
    }

    /**
     * @since 2.1.4
     */
    default Jt808MsgEncryptionType dataEncryptionType() {
        return Jt808MsgEncryptionType.fromIntValue(this.encryptionType());
    }

    /**
     * byte[2,4).bit[15] -- 保留位
     */
    default byte reversedBit15InHeader() {
        return 0;
    }

    Jt808Response reversedBit15InHeader(byte reversedBit15InHeader);

    /**
     * {@link Jt808ProtocolVersion#VERSION_2013 VERSION_2013} -- byte[4,10) -- {@link  MsgDataType#BCD BCD[6]} -- 终端手机号
     * <p>
     * {@link Jt808ProtocolVersion#VERSION_2019 VERSION_2019} -- byte[5,15) -- {@link  MsgDataType#BCD BCD[10]} -- 终端手机号
     */
    String terminalId();

    /**
     * {@link Jt808ProtocolVersion#VERSION_2013 VERSION_2013} -- byte[10,11) -- {@link  MsgDataType#WORD WORD} -- 流水号
     * <p>
     * {@link Jt808ProtocolVersion#VERSION_2019 VERSION_2019} -- byte[15,16) -- {@link  MsgDataType#WORD WORD} -- 流水号
     */
    int flowId();

    Jt808Response flowId(int flowId);

    /**
     * 响应消息大小超过该值(默认 {@value #DEFAULT_MAX_PACKAGE_SIZE})会自动分包发送(转义之前)
     *
     * @return 响应消息最大字节数
     * @see Jt808ResponseBody#maxPackageSize()
     */
    default int maxPackageSize() {
        return DEFAULT_MAX_PACKAGE_SIZE;
    }

    /**
     * 指定单个消息包的最大大小(转义之前)
     *
     * @param size 单个消息包的最大大小
     */
    Jt808Response maxPackageSize(int size);

    ByteBuf body();

    default void release() {
        JtProtocolUtils.release(this.body());
    }

    @Override
    default ByteBuf writable() {
        return body();
    }

    @Override
    default Jt808Response writeWord(int value) {
        Jt808ByteWriter.super.writeWord(value);
        return this;
    }

    @Override
    default Jt808Response writeDWord(int value) {
        Jt808ByteWriter.super.writeDWord(value);
        return this;
    }

    @Override
    default Jt808Response writeByte(int value) {
        Jt808ByteWriter.super.writeByte(value);
        return this;
    }

    @Override
    default Jt808Response writeBytes(ByteBuf byteBuf) {
        Jt808ByteWriter.super.writeBytes(byteBuf);
        return this;
    }

    @Override
    default Jt808Response writeBytes(byte[] bytes) {
        Jt808ByteWriter.super.writeBytes(bytes);
        return this;
    }

    @Override
    default Jt808Response writeBcd(String bcd) {
        Jt808ByteWriter.super.writeBcd(bcd);
        return this;
    }

    @Override
    default Jt808Response writeString(String value, Charset charset) {
        Jt808ByteWriter.super.writeString(value, charset);
        return this;
    }

    @Override
    default Jt808Response writeString(String string) {
        Jt808ByteWriter.super.writeString(string);
        return this;
    }

    @Override
    default Jt808Response clear() {
        Jt808ByteWriter.super.clear();
        return this;
    }

    static Jt808ResponseBuilder newBuilder() {
        return DefaultJt808ResponseBuilder.newBuilder();
    }

    interface Jt808ResponseBuilder {

        default Jt808ResponseBuilder msgId(MsgType msgType) {
            return this.msgId(msgType.getMsgId());
        }

        Jt808ResponseBuilder msgId(int msgId);

        Jt808ResponseBuilder version(Jt808ProtocolVersion version);

        Jt808ResponseBuilder encryptionType(int encryptionType);

        default Jt808ResponseBuilder encryptionType(Jt808MsgEncryptionType encryptionType) {
            return this.encryptionType(encryptionType.intValue());
        }

        Jt808ResponseBuilder reversedBit15InHeader(byte reversedBit15InHeader);

        Jt808ResponseBuilder terminalId(String terminalId);

        Jt808ResponseBuilder flowId(Integer flowId);

        Jt808ResponseBuilder body(ByteBuf body, boolean autoRelease);

        default Jt808ResponseBuilder body(ByteBuf body) {
            return this.body(body, true);
        }

        Jt808ResponseBuilder body(Consumer<Jt808ByteWriter> writer);

        Jt808ResponseBuilder maxPackageSize(int maxPackageSize);

        Jt808Response build();
    }

    interface Jt808ResponseSubPackage {

        int firstFlowIdOfSubPackageGroup();

        String terminalId();

        int msgId();

        int flowId();

        int totalSubPackageCount();

        int currentPackageNo();

        ByteBuf msg();

        LocalDateTime createdAt();

        Jt808ResponseSubPackage copy();

        static Jt808ResponseSubPackage ofDefault(
                String terminalId, int msgId, int firstFlowIdOfPackageGroup, int flowId, int totalSubPackageCount, int currentPackageNo,
                ByteBuf msg, LocalDateTime createdAt) {
            return new DefaultJt808ResponseSubPackage(
                    terminalId, msgId, firstFlowIdOfPackageGroup, flowId, totalSubPackageCount, currentPackageNo,
                    msg, createdAt
            );
        }
    }
}
