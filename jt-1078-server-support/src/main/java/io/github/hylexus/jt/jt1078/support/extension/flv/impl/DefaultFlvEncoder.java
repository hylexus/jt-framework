package io.github.hylexus.jt.jt1078.support.extension.flv.impl;

import io.github.hylexus.jt.common.JtCommonUtils;
import io.github.hylexus.jt.exception.JtIllegalStateException;
import io.github.hylexus.jt.jt1078.spec.Jt1078PayloadType;
import io.github.hylexus.jt.jt1078.spec.Jt1078Request;
import io.github.hylexus.jt.jt1078.spec.Jt1078RequestHeader;
import io.github.hylexus.jt.jt1078.spec.Jt1078SubscriberCreator;
import io.github.hylexus.jt.jt1078.spec.impl.request.DefaultJt1078PayloadType;
import io.github.hylexus.jt.jt1078.support.extension.audio.Jt1078AudioFormatConverter;
import io.github.hylexus.jt.jt1078.support.extension.audio.impl.BuiltinAudioFormatOptions;
import io.github.hylexus.jt.jt1078.support.extension.audio.impl.FlvJt1078AudioData;
import io.github.hylexus.jt.jt1078.support.extension.audio.impl.converters.*;
import io.github.hylexus.jt.jt1078.support.extension.flv.FlvEncoder;
import io.github.hylexus.jt.jt1078.support.extension.flv.FlvTagHeader;
import io.github.hylexus.jt.jt1078.support.extension.flv.tag.Amf;
import io.github.hylexus.jt.jt1078.support.extension.flv.tag.AudioFlvTag;
import io.github.hylexus.jt.jt1078.support.extension.flv.tag.ScriptFlvTag;
import io.github.hylexus.jt.jt1078.support.extension.flv.tag.VideoFlvTag;
import io.github.hylexus.jt.jt1078.support.extension.h264.*;
import io.github.hylexus.jt.jt1078.support.extension.h264.impl.DefaultH264Decoder;
import io.github.hylexus.jt.jt1078.support.extension.h264.impl.DefaultSpsDecoder;
import io.github.hylexus.jt.utils.Jdk8Adapter;
import io.github.hylexus.oaks.utils.IntBitOps;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author hylexus
 * @see <a href="https://rtmp.veriskope.com/pdf/video_file_format_spec_v10.pdf">video_file_format_spec_v10.pdf</a>
 * @see <a href="https://www.jianshu.com/p/916899d4833b">30分钟学会FLV</a>
 * @see <a href="https://www.jianshu.com/p/07657d85617e">FLV视频封装格式详细解析</a>
 * @see <a href="https://www.cnblogs.com/chyingp/p/flv-getting-started.html">FLV协议5分钟入门浅析</a>
 * @see <a href="https://www.cnblogs.com/CoderTian/p/8278369.html">FLV文件格式解析</a>
 * @see <a href="https://gitee.com/ldming/JT1078">https://gitee.com/ldming/JT1078</a>
 * @see <a href="https://gitee.com/matrixy/jtt1078-video-server">https://gitee.com/matrixy/jtt1078-video-server</a>
 * @see <a href="https://sample-videos.com/index.php#sample-flv-video">https://sample-videos.com/index.php#sample-flv-video</a>
 */
@Slf4j
public class DefaultFlvEncoder implements FlvEncoder {

    private final H264Decoder h264Decoder = new DefaultH264Decoder();

    private final ByteBufAllocator allocator = ByteBufAllocator.DEFAULT;

    private final SpsDecoder spsDecoder = new DefaultSpsDecoder();

    @Getter
    private ByteBuf flvBasicFrame;
    private ByteBuf spsFrame;
    private ByteBuf ppsFrame;
    @Getter
    private ByteBuf lastIFrame;
    @Getter
    private boolean hasAudio = true;

    private long baseTimestamp = -1;
    private long baseAudioTimestamp = -1;

    private final AdpcmImaToMp3Jt1078AudioFormatConverter adpcmImaToMp3Converter = new AdpcmImaToMp3Jt1078AudioFormatConverter();
    private final G726ToMp3Jt1078AudioFormatConverter g726ToMp3Converter = new G726ToMp3Jt1078AudioFormatConverter();
    private final G711aToMp3Jt1078AudioFormatConverter g711aToMp3Converter = new G711aToMp3Jt1078AudioFormatConverter();
    private final G711uToMp3Jt1078AudioFormatConverter g711uToMp3Converter = new G711uToMp3Jt1078AudioFormatConverter();

    private Jt1078AudioFormatConverter.AudioFormatOptions sourceAudioOptions;


    private Jt1078AudioFormatConverter audioFormatConverter;

    public DefaultFlvEncoder(Jt1078SubscriberCreator creator) {
        if (creator.sourceAudioOptions() == null) {
            return;
        }

        final Jt1078AudioFormatConverter.AudioFormatOptions sourceOptions = creator.sourceAudioOptions();
        if (sourceOptions instanceof BuiltinAudioFormatOptions) {
            final BuiltinAudioFormatOptions type = ((BuiltinAudioFormatOptions) sourceOptions);
            if (type.isG726()) {
                this.sourceAudioOptions = sourceOptions;
                this.audioFormatConverter = this.g726ToMp3Converter;
            } else if (type.isAdpcm()) {
                this.sourceAudioOptions = sourceOptions;
                this.audioFormatConverter = this.adpcmImaToMp3Converter;
            } else if (type.isG711()) {
                if (type == BuiltinAudioFormatOptions.G711_A_MONO) {
                    this.sourceAudioOptions = sourceOptions;
                    this.audioFormatConverter = this.g711aToMp3Converter;
                } else if (type == BuiltinAudioFormatOptions.G711_U_MONO) {
                    this.sourceAudioOptions = sourceOptions;
                    this.audioFormatConverter = this.g711uToMp3Converter;
                }
            } else if (type == BuiltinAudioFormatOptions.SILENCE) {
                this.sourceAudioOptions = sourceOptions;
                this.hasAudio = false;
                log.info("Audio data ignored. sim={}, channel={}, desc={} ", creator.sim(), creator.channelNumber(), creator.desc());
                this.audioFormatConverter = new SilenceJt1078AudioFormatConverter();
            }
        } else {
            throw new NotImplementedException();
        }
    }

    @Override
    public List<ByteBuf> encode(Jt1078Request request) {
        // 音频
        if (request.header().payloadType().isAudio()) {
            return this.doEncodeAudio(request);
            // return Collections.emptyList();
        }
        // 视频
        final List<H264Nalu> h264NaluList;
        try {
            h264NaluList = this.h264Decoder.decode(request);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Collections.emptyList();
        }
        final List<ByteBuf> list = new ArrayList<>(h264NaluList.size());
        for (final H264Nalu nalu : h264NaluList) {
            final Optional<ByteBuf> data = this.doEncode(nalu);
            data.ifPresent(list::add);
        }
        return list;
    }

    Jt1078AudioFormatConverter.Jt1078AudioData convertAudio(Jt1078Request request) {
        // todo 优化代码
        final ByteBuf mayBeChanged = beforeConvert(request.body());
        if (this.audioFormatConverter != null) {
            return this.audioFormatConverter.convert(beforeConvert(mayBeChanged), this.sourceAudioOptions);
        }

        final Jt1078AudioFormatConverter converter = this.initDefaultAudioConverter(request.header());
        if (converter == null) {
            return Jt1078AudioFormatConverter.Jt1078AudioData.empty();
        }
        return converter.convert(mayBeChanged, this.sourceAudioOptions);
    }

    private List<ByteBuf> doEncodeAudio(Jt1078Request request) {
        try (final Jt1078AudioFormatConverter.Jt1078AudioData audio = this.convertAudio(request)) {
            if (audio.isEmpty()) {
                return Collections.emptyList();
            }

            if (audio instanceof FlvJt1078AudioData) {
                return this.doEncodeFlvAudioTag(request, (FlvJt1078AudioData) audio);
            } else {
                throw new JtIllegalStateException("Unsupported AudioType " + audio.getClass());
            }
        }
    }

    protected ByteBuf beforeConvert(ByteBuf stream) {
        // 参考: https://www.hentai.org.cn/article?id=8
        // 参考: https://www.hentai.org.cn/article?id=8
        // 参考: https://www.hentai.org.cn/article?id=8
        // 音频数据体通常会带上"海思"头，它的形式如 00 01 XX 00
        // XX表示后续字节数的一半，如果碰到符合这个规则的前四个字节，直接去掉就可以了
        if (stream.getByte(0) == 0
                && stream.getByte(1) == 1
                && stream.getByte(2) == (stream.readableBytes() - 4) >>> 1
                && stream.getByte(3) == 0) {
            stream.readerIndex(stream.readerIndex() + 4);
        }
        return stream;
    }

    private Jt1078AudioFormatConverter initDefaultAudioConverter(Jt1078RequestHeader header) {
        final Jt1078PayloadType payloadType = header.payloadType();
        if (payloadType == DefaultJt1078PayloadType.ADPCMA) {
            this.sourceAudioOptions = BuiltinAudioFormatOptions.ADPCM_IMA_MONO;
            this.audioFormatConverter = this.adpcmImaToMp3Converter;
            return this.audioFormatConverter;
        } else if (payloadType == DefaultJt1078PayloadType.G_726) {
            this.sourceAudioOptions = BuiltinAudioFormatOptions.G726_S32_LE_MONO;
            this.audioFormatConverter = this.g726ToMp3Converter;
            return this.audioFormatConverter;
        } else if (payloadType == DefaultJt1078PayloadType.G_711A) {
            this.sourceAudioOptions = BuiltinAudioFormatOptions.G711_A_MONO;
            this.audioFormatConverter = this.g711aToMp3Converter;
            return this.audioFormatConverter;
        } else if (payloadType == DefaultJt1078PayloadType.G_711U) {
            this.sourceAudioOptions = BuiltinAudioFormatOptions.G711_U_MONO;
            this.audioFormatConverter = this.g711uToMp3Converter;
            return this.audioFormatConverter;
        } else {
            log.info("Audio data ignored. sim={}, channel={}, payloadType={} ", header.sim(), header.channelNumber(), payloadType);
            this.sourceAudioOptions = BuiltinAudioFormatOptions.SILENCE;
            this.audioFormatConverter = new SilenceJt1078AudioFormatConverter();
            this.hasAudio = false;
            return this.audioFormatConverter;
        }
    }

    private List<ByteBuf> doEncodeFlvAudioTag(Jt1078Request request, FlvJt1078AudioData audio) {
        final ByteBuf buffer = this.allocator.buffer();
        try {
            // 1. tagHeader : 11 bytes
            final int pts = this.calculateAudioTimestamp(request.header().timestamp().orElse(0L));
            final int outerHeaderSize = FlvTagHeader.newAudioTagHeader(0, pts).writeTo(buffer);

            // 2.1 tagData[audioTagHeader] : 1 bytes / 2 bytes(AAC)
            final AudioFlvTag.AudioFlvTagHeader audioTagHeader = audio.flvTagHeader();
            final int audioHeaderSize = audioTagHeader.writeTo(buffer);

            // 2.2 tagData[audioTagData]
            buffer.writeBytes(audio.payload());

            // 2.3 给 3 字节的 dataSize 赋值(之前的 0 只是个占位符)
            final int tagDataSize = audioHeaderSize + audio.payloadSize();
            buffer.setBytes(1, IntBitOps.intTo3Bytes(tagDataSize));

            // 3. previous tag size: 11 + tagDataSize
            buffer.writeInt(outerHeaderSize + tagDataSize);
            return Jdk8Adapter.listOf(buffer);
        } catch (Throwable throwable) {
            buffer.release();
            throw throwable;
        }
    }

    private Optional<ByteBuf> doEncode(H264Nalu nalu) {
        final Optional<H264NaluHeader.NaluType> optional = nalu.header().type();
        if (!optional.isPresent()) {
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
            this.flvBasicFrame = this.createFlvBasicFrame(nalu.timestamp().orElse(0L));
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
            // final int pts = nalu.timestamp().orElse(0L).intValue();
            final int pts = this.calculateTimestamp(nalu);
            final int outerHeaderSize = FlvTagHeader.newVideoTagHeader(0, pts).writeTo(buffer);

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

    private int calculateTimestamp(H264Nalu nalu) {
        final long ts = nalu.timestamp().orElse(0L);
        if (this.baseTimestamp < 0) {
            this.baseTimestamp = ts;
        }

        return (int) (ts - this.baseTimestamp);
    }

    private int calculateAudioTimestamp(long ts) {
        if (this.baseAudioTimestamp < 0) {
            this.baseAudioTimestamp = ts;
        }

        return (int) (ts - this.baseAudioTimestamp);
    }

    private ByteBuf createFlvBasicFrame(long ts) {
        final Sps sps = this.spsDecoder.decodeSps(this.spsFrame);
        final ByteBuf buffer = this.allocator.buffer();
        this.flvBasicFrame = buffer;
        this.writeScriptTag(buffer, sps);
        this.writeFirstVideoTag(buffer, sps);
        // this.baseAudioTimestamp = ts;
        // this.baseTimestamp = ts;
        return buffer;
    }

    private void writeScriptTag(ByteBuf buffer, Sps sps) {
        final int writerIndex = buffer.writerIndex();
        // 1. tagHeader: 11 bytes
        final int outerHeaderSize = FlvTagHeader.newScriptTagHeader(0, 0).writeTo(buffer);
        // 2.1 tagData: scriptTag 没有 header 直接就是数据
        final int tagDataSize = ScriptFlvTag.of(this.generateMetadata(sps)).writeTo(buffer);

        // 2.2 给 3 字节的 dataSize 赋值(之前的 0 只是个占位符)
        buffer.setBytes(writerIndex + 1, IntBitOps.intTo3Bytes(tagDataSize));
        // 3. previous tag size: 11 + tagDataSize
        buffer.writeInt(outerHeaderSize + tagDataSize);
    }

    private List<Amf> generateMetadata(Sps sps) {
        return Jdk8Adapter.listOf(
                Amf.ofString("onMetaData"),
                Amf.ofEcmaArray(Jdk8Adapter.listOf(
                        Amf.ofPair("videocodecid", Amf.ofNumber(VideoFlvTag.VideoCodecId.AVC.getValue() * 1.0)),
                        Amf.ofPair("width", Amf.ofNumber(sps.getWidth() * 1.0D)),
                        Amf.ofPair("height", Amf.ofNumber(sps.getHeight() * 1.0D))
                ))
        );
    }

    private void writeFirstVideoTag(ByteBuf buffer, Sps sps) {
        final int writerIndex = buffer.writerIndex();
        // 1. tagHeader: 11 bytes
        final int outerHeaderSize = FlvTagHeader.newVideoTagHeader(0, 0).writeTo(buffer);
        // 2.1 tagData[videoTagHeader] : 5 bytes
        // 4bits(frameType) + 4bits(codecId) + 8bits(avcPacketType) + compositionTime(24bits) = 5 bytes
        final int videoHeaderSize = VideoFlvTag.VideoFlvTagHeader.createAvcSequenceHeader().writeTo(buffer);
        // 2.2 tagData[videoTagData]
        final int videoBodySize = AvcDecoderConfigurationRecord.writeTo(buffer, this.spsFrame, this.ppsFrame, sps);
        // 2.3 给 3 字节的 dataSize 赋值(之前的 0 只是个占位符)
        final int tagDataSize = videoHeaderSize + videoBodySize;
        buffer.setBytes(writerIndex + 1, IntBitOps.intTo3Bytes(tagDataSize));
        // 3. previous tag size: 11 + tagDataSize
        buffer.writeInt(outerHeaderSize + tagDataSize);
    }

    @Override
    public void close() {
        try {
            this.g726ToMp3Converter.close();
        } catch (Exception ignore) {
            // ignore
        }

        try {
            this.adpcmImaToMp3Converter.close();
        } catch (Exception ignore) {
            // ignore
        }

        JtCommonUtils.release(this.flvBasicFrame, this.spsFrame, this.ppsFrame, this.lastIFrame);
    }
}
