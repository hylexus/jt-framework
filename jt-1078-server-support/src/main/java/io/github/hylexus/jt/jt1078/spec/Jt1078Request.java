package io.github.hylexus.jt.jt1078.spec;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.common.JtCommonUtils;
import io.github.hylexus.jt.jt1078.spec.impl.request.DefaultJt1078Request;
import io.netty.buffer.ByteBuf;

import java.util.Optional;

/**
 * @author hylexus
 */
@BuiltinComponent
public interface Jt1078Request {
    /**
     * 帧头标识
     */
    int FRAME_HEADER_IDENTIFIER = 0X30316364;

    /**
     * 数据类型
     *
     * @param offset15 消息头中第15个字节
     * @return 高四位
     */
    static byte dataType(short offset15) {
        return (byte) ((offset15 >> 4) & 0x0f);
    }

    default byte dataType() {
        return dataType(this.offset15());
    }

    /**
     * 分包处理标记
     *
     * @param offset15 消息头中第15个字节
     * @return 低四位
     */
    static byte subPackageIdentifier(short offset15) {
        return (byte) (offset15 & 0x0f);
    }

    default byte subPackageIdentifier() {
        return subPackageIdentifier(this.offset15());
    }

    static boolean isAudioData(byte dataType) {
        return (dataType & 0b11) == 0b11;
    }

    static boolean isVideoData(byte dataType) {
        // 0000: 视频I帧
        // 0001: 视频P帧
        // 0010: 视频B帧
        // 0011: 音频帧
        // 0100: 透传数据
        return (dataType & 0b11) <= 0b10;
    }

    static boolean hasFrameIntervalFields(byte dataType) {
        return isVideoData(dataType);
    }

    static boolean hasTimestampField(byte dataType) {
        return (dataType & 0x0f) != 0b0100;
    }

    static int msgLengthFieldIndex(boolean hasTimestampField, boolean hasFrameIntervalFields) {
        final int index = 28;
        if (!hasTimestampField && !hasFrameIntervalFields) {
            return index - 2 - 2 - 8;
        } else if (!hasFrameIntervalFields) {
            return index - 2 - 2;
        }
        return index;
    }

    static Jt1078RequestBuilder newBuilder() {
        return new DefaultJt1078Request();
    }

    ByteBuf rawByteBuf();

    /**
     * 请求体
     */
    ByteBuf body();

    /**
     * bytes[4,5)
     */
    short offset4();

    @SuppressWarnings("checkstyle:methodname")
    default byte v() {
        return (byte) ((this.offset4() >> 6) & 0b11);
    }

    @SuppressWarnings("checkstyle:methodname")
    default byte p() {
        return (byte) ((this.offset4() >> 5) & 0b01);
    }

    @SuppressWarnings("checkstyle:methodname")
    default byte x() {
        return (byte) ((this.offset4() >> 4) & 0b01);
    }

    @SuppressWarnings("checkstyle:methodname")
    default byte cc() {
        return (byte) (this.offset4() & 0x0f);
    }

    /**
     * bytes[5,6)
     */
    short offset5();

    @SuppressWarnings("checkstyle:methodname")
    default byte m() {
        return (byte) ((this.offset5() >> 7) & 0b01);
    }

    default byte pt() {
        return (byte) (this.offset5() & 0b0111_1111);
    }

    /**
     * bytes[6,8)    包序号
     */
    int sequenceNumber();

    /**
     * bytes[8,14)    BCD[6]    SIM卡号
     */
    String sim();

    /**
     * bytes[14,15)    逻辑通道号
     */
    short channelNumber();

    /**
     * bytes[15,16)
     */
    short offset15();


    /**
     * bytes[16,24)    BYTE[8] 时间戳
     *
     * @return 当 {@link #dataType()} == 0x0100 时返回 {@link Optional#empty()}
     */
    Optional<Long> timestamp();

    /**
     * bytes[24,26)    WORD    该帧与上一个关键帧之间的相对时间(ms)
     *
     * @return 当 {@link #dataType()} == 0x0100 || {@link #dataType()} == 0x0011 时返回 {@link Optional#empty()}
     * @see #dataType(short)
     * @see #dataType()
     * @see #hasFrameIntervalFields(byte)
     * @see #lastFrameInterval()
     */
    Optional<Integer> lastIFrameInterval();

    /**
     * bytes[24,26)    WORD    该帧与上一帧之间的相对时间(ms)
     *
     * @return 当 {@link #dataType()} == 0x0100 || {@link #dataType()} == 0x0011 时返回 {@link Optional#empty()}
     * @see #dataType(short)
     * @see #dataType()
     * @see #hasFrameIntervalFields(byte)
     * @see #lastIFrameInterval()
     */
    Optional<Integer> lastFrameInterval();

    /**
     * bytes[28,30)    WORD    数据体长度
     */
    int msgBodyLength();

    default void release() {
        JtCommonUtils.release(this.rawByteBuf(), this.body());
    }

    default Jt1078RequestBuilder mutate() {
        return new DefaultJt1078Request(this);
    }

    interface Jt1078RequestBuilder {

        Jt1078RequestBuilder offset4(short offset4);

        Jt1078RequestBuilder offset5(short offset5);

        Jt1078RequestBuilder sequenceNumber(int sequenceNumber);

        Jt1078RequestBuilder sim(String sim);

        Jt1078RequestBuilder channelNumber(short channelNumber);

        Jt1078RequestBuilder offset15(short offset15);

        Jt1078RequestBuilder timestamp(Long timestamp);

        Jt1078RequestBuilder lastIFrameInterval(Integer lastIFrameInterval);

        Jt1078RequestBuilder lastFrameInterval(Integer lastFrameInterval);

        default Jt1078RequestBuilder body(ByteBuf body) {
            return this.body(body, true);
        }

        Jt1078RequestBuilder body(ByteBuf body, boolean autoRelease);

        default Jt1078RequestBuilder rawByteBuf(ByteBuf rawByteBuf) {
            return this.rawByteBuf(rawByteBuf, true);
        }

        Jt1078RequestBuilder rawByteBuf(ByteBuf rawByteBuf, boolean autoRelease);

        Jt1078Request build();
    }
}
