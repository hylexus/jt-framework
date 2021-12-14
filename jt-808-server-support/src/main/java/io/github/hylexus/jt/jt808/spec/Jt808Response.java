package io.github.hylexus.jt.jt808.spec;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.impl.response.DefaultJt808ResponseBuilder;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.result.Jt808ResponseHandlerResultHandler;
import io.github.hylexus.jt.utils.JtProtocolUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * @author hylexus
 * @see Jt808ResponseHandlerResultHandler
 */
public interface Jt808Response {

    int DEFAULT_MAX_PACKAGE_SIZE = 1024;

    default ByteBufAllocator allocator() {
        return ByteBufAllocator.DEFAULT;
    }

    /**
     * byte[0,2) -- {@link  MsgDataType#WORD WORD} -- 消息ID
     */
    int msgType();

    Jt808Response msgType(int msgType);

    Jt808ProtocolVersion version();

    /**
     * byte[2,4).bit[0,9) -- 消息体长度
     * <p>
     * byte[2,4).bit[13] -- {@link #maxPackageSize()}
     * <p>
     * byte[2,4).bit[14] -- {@link #msgType()}
     */
    default int msgBodyLength() {
        return body().readableBytes();
    }

    // /**
    //  * byte[2,4).bit[10,12) -- 数据加密方式
    //  */
    // default int encryptionType() {
    //     return 0;
    // }

    // Jt808Response encryptionType(int encType);

    /**
     * byte[2,4).bit[15] -- 保留位
     */
    default byte reversedBit15InHeader() {
        return 0;
    }

    Jt808Response reversedBit15InHeader(byte reversedBit15InHeader);

    /**
     * {@link Jt808ProtocolVersion#VERSION_2011 VERSION_2011} -- byte[4,10) -- {@link  MsgDataType#BCD BCD[6]} -- 终端手机号
     * <p>
     * {@link Jt808ProtocolVersion#VERSION_2019 VERSION_2019} -- byte[5,15) -- {@link  MsgDataType#BCD BCD[10]} -- 终端手机号
     */
    String terminalId();

    /**
     * {@link Jt808ProtocolVersion#VERSION_2011 VERSION_2011} -- byte[10,11) -- {@link  MsgDataType#WORD WORD} -- 流水号
     * <p>
     * {@link Jt808ProtocolVersion#VERSION_2019 VERSION_2019} -- byte[15,16) -- {@link  MsgDataType#WORD WORD} -- 流水号
     */
    int flowId();

    Jt808Response flowId(int flowId);

    default int maxPackageSize() {
        return DEFAULT_MAX_PACKAGE_SIZE;
    }

    Jt808Response maxPackageSize(int size);

    ByteBuf body();

    default Jt808Response writeWord(int value) {
        JtProtocolUtils.writeWord(body(), value);
        return this;
    }

    default Jt808Response writeDWord(int value) {
        JtProtocolUtils.writeDword(body(), value);
        return this;
    }

    default Jt808Response writeByte(int value) {
        body().writeByte(value);
        return this;
    }

    default Jt808Response writeBytes(ByteBuf byteBuf) {
        body().writeBytes(byteBuf);
        return this;
    }

    default Jt808Response writeBytes(byte[] bytes) {
        body().writeBytes(bytes);
        return this;
    }

    default Jt808Response writeBcd(String bcd) {
        JtProtocolUtils.writeBcd(body(), bcd);
        return this;
    }

    default Jt808Response clear() {
        body().clear();
        return this;
    }

    static DefaultJt808ResponseBuilder newBuilder() {
        return DefaultJt808ResponseBuilder.newBuilder();
    }
}
