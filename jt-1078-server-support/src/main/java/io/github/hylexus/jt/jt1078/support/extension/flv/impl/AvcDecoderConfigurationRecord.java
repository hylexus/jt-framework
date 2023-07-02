package io.github.hylexus.jt.jt1078.support.extension.flv.impl;

import io.github.hylexus.jt.jt1078.support.extension.h264.Sps;
import io.netty.buffer.ByteBuf;

/**
 * 最终结果是 N 个 8bits(aligned(8)) 也就是 N 个字节:
 * 8bits + 8bits + 8bits + 8bits + (6bits+2bits) + (3bits + 5bits) + numOfSequenceParameterSets * (16bits + m * 8bits) +  numOfPictureParameterSets * (16bits + n* 8bits)
 * <p>
 * 详情见 ISO 14496-12，下面是文档中的伪代码:
 *
 * <pre>
 * {@code
 * aligned(8) class AVCDecoderConfigurationRecord {
 *     unsigned int(8) configurationVersion = 1; // 1 字节；固定为 0x01
 *     unsigned int(8) AVCProfileIndication; // 1 字节；SPS[0]
 *     unsigned int(8) profile_compatibility; // 1字节 SPS[1]
 *     unsigned int(8) AVCLevelIndication; // 1字节 SPS[2]
 *     bit(6) reserved = ‘111111’b; // 6bit；保留位；固定为 0b111111
 *     unsigned int(2) lengthSizeMinusOne; // 2bits；长度-1；0表示1字节,1表示2字节,2表示3字节,3表示4字节
 *     bit(3) reserved = ‘111’b; // 3bit； 保留位；固定为 0b111
 *
 *     unsigned int(5) numOfSequenceParameterSets; // 5bit；sps的个数
 *     for (i=0; i< numOfSequenceParameterSets; i++) {
 *         unsigned int(16) sequenceParameterSetLength ; // 2bytes；下一个 sps 的长度
 *         bit(8*sequenceParameterSetLength) sequenceParameterSetNALUnit; // sps 数据
 *     }
 *
 *     unsigned int(8) numOfPictureParameterSets; // 1byte；pps 的个数
 *     for (i=0; i< numOfPictureParameterSets; i++) {
 *         unsigned int(16) pictureParameterSetLength; // 2bytes；下一个 pps 的长度
 *         bit(8*pictureParameterSetLength) pictureParameterSetNALUnit; // pps 数据
 *     }
 * }
 * }
 * </pre>
 *
 * @see <a href="https://zhuanlan.zhihu.com/p/469580990">RTMP协议封装H264格式详解</a>
 * @see <a href="https://blog.csdn.net/fanyun_01/article/details/108305456">H.264/AVC编码的FLV文件的第二个Tag: AVCDecoderConfigurationRecord</a>
 * @see <a href="https://blog.csdn.net/weixin_39399492/article/details/129986667">FLV格式详解</a>
 */
public class AvcDecoderConfigurationRecord {

    public AvcDecoderConfigurationRecord() {
    }

    public static int writeTo(ByteBuf byteBuf, ByteBuf spsData, ByteBuf ppsData, Sps sps) {
        final int start = byteBuf.writerIndex();
        // configurationVersion=1
        byteBuf.writeByte(1);
        // avcProfileIndication
        byteBuf.writeByte(sps.getProfileIdc());
        // profileCompatibility
        byteBuf.writeByte(sps.getProfileCompat());
        // avcLevelIndication
        byteBuf.writeByte(sps.getLevelIdc());
        // 6bits(reserved) + 2bits(lengthSizeMinusOne)
        // 3 <-- (4 - 1) bytes 的 NALU 头长度
        // (0b111111<<2) | (3) == 0xff
        byteBuf.writeByte(0xff);
        // 3bits(reserved) + 5bits(numOfSequenceParameterSets)
        // (0b111<<5) | (1 & 0b11111) == 0b11100001 == 0xe1
        byteBuf.writeByte(0xe1);

        // length: sequenceParameterSetLength
        final int spsLength = spsData.readableBytes();
        // sps 长度
        byteBuf.writeShort(spsLength);
        // sps 数据 sequenceParameterSetNALUnit
        byteBuf.writeBytes(spsData, spsLength);

        // ====== pps ==================
        // pps个数: numOfPictureParameterSets
        byteBuf.writeByte(1);
        // pps 长度 pictureParameterSetLength
        final int ppsLength = ppsData.readableBytes();
        byteBuf.writeShort(ppsLength);
        // pps 数据 pictureParameterSetNALUnit
        byteBuf.writeBytes(ppsData, ppsLength);
        return byteBuf.writerIndex() - start;
    }
}
