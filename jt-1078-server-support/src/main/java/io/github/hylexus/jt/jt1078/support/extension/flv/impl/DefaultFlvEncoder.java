package io.github.hylexus.jt.jt1078.support.extension.flv.impl;

import io.github.hylexus.jt.jt1078.spec.Jt1078Request;
import io.github.hylexus.jt.jt1078.support.extension.flv.FlvEncoder;
import io.github.hylexus.jt.jt1078.support.extension.flv.FlvTagHeader;
import io.github.hylexus.jt.jt1078.support.extension.flv.tag.VideoFlvTag;
import io.github.hylexus.jt.jt1078.support.extension.h264.H264Decoder;
import io.github.hylexus.jt.jt1078.support.extension.h264.H264Nalu;
import io.github.hylexus.jt.jt1078.support.extension.h264.H264NaluHeader;
import io.github.hylexus.jt.jt1078.support.extension.h264.impl.DefaultH264Decoder;
import io.github.hylexus.oaks.utils.IntBitOps;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author hylexus
 * @see <a href="https://www.jianshu.com/p/916899d4833b">30分钟学会FLV</a>
 * @see <a href="https://www.cnblogs.com/chyingp/p/flv-getting-started.html">FLV协议5分钟入门浅析</a>
 * @see <a href="https://www.cnblogs.com/CoderTian/p/8278369.html">FLV文件格式解析</a>
 * @see <a href="https://gitee.com/ldming/JT1078">https://gitee.com/ldming/JT1078</a>
 * @see <a href="https://gitee.com/matrixy/jtt1078-video-server">https://gitee.com/matrixy/jtt1078-video-server</a>
 */
@Slf4j
public class DefaultFlvEncoder implements FlvEncoder {

    private final H264Decoder h264Decoder = new DefaultH264Decoder();

    private final ByteBufAllocator allocator = ByteBufAllocator.DEFAULT;

    private ByteBuf flvBasicFrame;
    private ByteBuf spsFrame;
    private ByteBuf ppsFrame;
    @Getter
    private ByteBuf lastIFrame;

    @Override
    public List<ByteBuf> encode(Jt1078Request request) {
        final List<H264Nalu> h264NaluList = this.h264Decoder.decode(request);
        final List<ByteBuf> list = new ArrayList<>(h264NaluList.size());
        for (final H264Nalu nalu : h264NaluList) {
            final Optional<ByteBuf> data = this.doEncode(nalu);
            data.ifPresent(list::add);
        }
        return list;
    }

    public ByteBuf getFlvBasicFrame() {
        return flvBasicFrame;
    }

    private Optional<ByteBuf> doEncode(H264Nalu nalu) {
        final Optional<H264NaluHeader.NaluType> optional = nalu.header().type();
        if (optional.isEmpty()) {
            return Optional.empty();
        }

        final H264NaluHeader.NaluType naluType = optional.get();
        // type in [1,2,3,4,5,7,8]
        if (naluType.getValue() <= 0 || naluType.getValue() > 8 || naluType.getValue() == 6) {
            return Optional.empty();
        }

        if (naluType == H264NaluHeader.NaluType.SPS && this.spsFrame == null) {
            this.spsFrame = this.allocator.buffer();
            this.spsFrame.writeBytes(nalu.data(), nalu.data().readableBytes());
        }

        if (naluType == H264NaluHeader.NaluType.PPS && this.ppsFrame == null) {
            this.ppsFrame = this.allocator.buffer();
            this.ppsFrame.writeBytes(nalu.data(), nalu.data().readableBytes());
        }

        if (this.ppsFrame != null && this.spsFrame != null && this.flvBasicFrame == null) {
            this.flvBasicFrame = this.createFlvBasicFrame();
        }

        if (this.flvBasicFrame == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(this.createFlvFrame(nalu, naluType));
    }

    private ByteBuf createFlvFrame(H264Nalu nalu, H264NaluHeader.NaluType naluType) {
        if (naluType == H264NaluHeader.NaluType.SPS || naluType == H264NaluHeader.NaluType.PPS || naluType == H264NaluHeader.NaluType.SEI) {
            return null;
        }

        final ByteBuf buffer = this.allocator.buffer();
        try {
            // 1. tagHeader : 11 bytes
            final int outerHeaderSize = FlvTagHeader.newVideoTagHeader(0, nalu.timestamp().orElse(0L).intValue()).writeTo(buffer);

            // 2.1 tagData[videoTagHeader] : 5 bytes
            final VideoFlvTag.VideoFrameType frameType = naluType == H264NaluHeader.NaluType.IDR
                    ? VideoFlvTag.VideoFrameType.KEY_FRAME
                    : VideoFlvTag.VideoFrameType.INTER_FRAME;
            final int cts = nalu.lastFrameInterval().orElse(0);
            final int videoHeaderSize = VideoFlvTag.VideoFlvTagHeader.createAvcNaluHeader(frameType, cts).writeTo(buffer);

            // 2.2 tagData[videoTagData]
            final int naluLength = nalu.data().readableBytes();
            buffer.writeInt(naluLength);
            buffer.writeBytes(nalu.data(), naluLength);

            // 2.3 给 3 字节的 dataSize 赋值(之前的 0 只是个占位符)
            // +4: `naluLength` 本身的长度 4 bytes
            final int tagDataSize = videoHeaderSize + naluLength + 4;
            buffer.setBytes(1, IntBitOps.intTo3Bytes(tagDataSize));

            // 3. previous tag size: 11 + tagDataSize
            buffer.writeInt(outerHeaderSize + tagDataSize);
            if (naluType == H264NaluHeader.NaluType.IDR) {
                if (this.lastIFrame != null) {
                    this.lastIFrame.release();
                }
                this.lastIFrame = buffer.copy();
            }
            return buffer;
        } catch (Throwable throwable) {
            buffer.release();
            throw throwable;
        }
    }

    private ByteBuf createFlvBasicFrame() {
        final ByteBuf buffer = this.allocator.buffer();
        this.flvBasicFrame = buffer;
        // 1. tagHeader: 11 bytes
        final int outerHeaderSize = FlvTagHeader.newVideoTagHeader(0, 0).writeTo(buffer);
        // 2.1 tagData[videoTagHeader] : 5 bytes
        // 4bits(frameType) + 4bits(codecId) + 8bits(avcPacketType) + compositionTime(24bits) = 5 bytes
        final int videoHeaderSize = VideoFlvTag.VideoFlvTagHeader.createAvcSequenceHeader().writeTo(buffer);
        // 2.2 tagData[videoTagData]
        final int videoBodySize = AvcDecoderConfigurationRecord.writeTo(buffer, this.spsFrame, this.ppsFrame);
        // 2.3 给 3 字节的 dataSize 赋值(之前的 0 只是个占位符)
        final int tagDataSize = videoHeaderSize + videoBodySize;
        buffer.setBytes(1, IntBitOps.intTo3Bytes(tagDataSize));
        // 3. previous tag size: 11 + tagDataSize
        buffer.writeInt(outerHeaderSize + tagDataSize);
        return buffer;
    }
}
