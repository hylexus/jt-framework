package io.github.hylexus.jt.jt1078.support.extension.h264.impl;

import io.github.hylexus.jt.jt1078.support.extension.h264.Sps;
import io.github.hylexus.jt.jt1078.support.extension.h264.SpsDecoder;
import io.github.hylexus.jt.utils.BitStreamReader;
import io.netty.buffer.ByteBuf;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hylexus
 * @see <a href="https://github.com/redknotmiaoyuqiao/EyerH264Decoder/blob/59e0a396f0b9c0a4a2c034ba6d44fae22297e979/EyerH264Decoder/NaluPPS.cpp#L11">Github--redknotmiaoyuqiao--EyerH264Decoder/NaluPPS.cpp</a>
 * @see <a href="https://github.com/SmallChi/JT1078/blob/c4931a8ec37678fb5ecedcc749ffe3c9f4c890fe/src/JT1078.Protocol/MessagePack/EXPGolombReader.cs#L24C20-L24C20">Github--SmallChi--JT1078/EXPGolombReader.cs</a>
 * @see <a href="https://www.itu.int/rec/T-REC-H.264">T-REC-H.264-202108-I!!PDF-E.pdf</a>
 */
public class DefaultSpsDecoder implements SpsDecoder {

    /**
     * @see <a href="https://www.itu.int/rec/T-REC-H.264">T-REC-H.264-202108-I!!PDF-E.pdf -- Table E-1 Meaning of sample aspect ratio indicator</a>
     */
    private static final Map<Integer, int[]> ASPECT_RATIO_IDC_MAPPING = new HashMap<>();

    static {
        ASPECT_RATIO_IDC_MAPPING.put(1, new int[]{1, 1});
        ASPECT_RATIO_IDC_MAPPING.put(2, new int[]{12, 11});
        ASPECT_RATIO_IDC_MAPPING.put(3, new int[]{10, 11});
        ASPECT_RATIO_IDC_MAPPING.put(4, new int[]{16, 11});
        ASPECT_RATIO_IDC_MAPPING.put(5, new int[]{40, 33});
        ASPECT_RATIO_IDC_MAPPING.put(6, new int[]{24, 11});
        ASPECT_RATIO_IDC_MAPPING.put(7, new int[]{20, 11});
        ASPECT_RATIO_IDC_MAPPING.put(8, new int[]{32, 11});
        ASPECT_RATIO_IDC_MAPPING.put(9, new int[]{80, 33});
        ASPECT_RATIO_IDC_MAPPING.put(10, new int[]{18, 11});
        ASPECT_RATIO_IDC_MAPPING.put(11, new int[]{15, 11});
        ASPECT_RATIO_IDC_MAPPING.put(12, new int[]{64, 33});
        ASPECT_RATIO_IDC_MAPPING.put(13, new int[]{160, 99});
        ASPECT_RATIO_IDC_MAPPING.put(14, new int[]{4, 3});
        ASPECT_RATIO_IDC_MAPPING.put(15, new int[]{3, 2});
        ASPECT_RATIO_IDC_MAPPING.put(16, new int[]{2, 1});
        // 17..254 reversed
        // 255: Extended_SAR
    }

    /**
     * @see <a href="https://www.jianshu.com/p/42be5bcb0a52">从H.264码流中一眼读出其Profile和Level</a>
     * @see <a href="https://www.bilibili.com/video/BV1nG4y1u7HT">https://www.bilibili.com/video/BV1nG4y1u7HT</a>
     */
    @Override
    public Sps decodeSps(ByteBuf input) {
        try (BitStreamReader reader = BitStreamReader.fromByteBuf(input.retainedSlice())) {
            return this.doDecodeSps(reader);
        }
    }

    private Sps doDecodeSps(BitStreamReader reader) {
        reader.readU8();
        // 1. profile_idc
        final int profileIdc = reader.readU8();

        // 2.1 profileCompat
        final int profileCompat = reader.readU6();
        // 2.2 reserved_zero_2bits /* equal to 0 */
        reader.readU2();

        // 3. level_idc
        final int levelIdc = reader.readU8();
        // 4. seq_parameter_set_id
        reader.readUe();
        if (profileIdc == 100 || profileIdc == 110
                || profileIdc == 122 || profileIdc == 244 || profileIdc == 44
                || profileIdc == 83 || profileIdc == 86 || profileIdc == 118
                || profileIdc == 128 || profileIdc == 138 || profileIdc == 139
                || profileIdc == 134 || profileIdc == 135) {
            // chroma_format_idc
            // 0: 单色
            // 1: yuv 4:2:0
            // 2: yuv 4:2:2
            // 3: yuv 4:4:4
            final int chromaFormatIdc = reader.readUe();
            if (chromaFormatIdc == 3) {
                // separate_colour_plane_flag
                reader.readU1();
            }

            // bit_depth_luma_minus8
            reader.readUe();
            // bit_depth_chroma_minus8
            reader.readUe();
            // qpprime_y_zero_transform_bypass_flag
            reader.readU1();
            // seq_scaling_matrix_present_flag
            final int seqScalingMatrixPresentFlag = reader.readU1();
            if (seqScalingMatrixPresentFlag == 1) {
                for (int i = 0; i < ((chromaFormatIdc != 3) ? 8 : 12); i++) {
                    // seq_scaling_list_present_flag[ i ]
                    final int seqScalingListPresentFlag = reader.readU1();
                    if (seqScalingListPresentFlag == 1) {
                        if (i < 6) {
                            scalingList(16, reader);
                        } else {
                            scalingList(64, reader);
                        }
                    }
                }
            }
        }

        // log2_max_frame_num_minus4
        reader.readUe();
        // pic_order_cnt_type
        final int picOrderCntType = reader.readUe();
        if (picOrderCntType == 0) {
            // log2_max_pic_order_cnt_lsb_minus4
            reader.readUe();
        } else if (picOrderCntType == 1) {
            // delta_pic_order_always_zero_flag
            reader.readU1();
            // offset_for_non_ref_pic
            reader.readSe();
            // offset_for_top_to_bottom_field
            reader.readSe();
            // num_ref_frames_in_pic_order_cnt_cycle
            final int numRefFramesInPicOrderCntCycle = reader.readUe();
            for (int i = 0; i < numRefFramesInPicOrderCntCycle; i++) {
                // offset_for_ref_frame[ i ]  se(v)
                reader.readSe();
            }
        }

        // max_num_ref_frames
        reader.readUe();
        // gaps_in_frame_num_value_allowed_flag
        reader.readU1();
        // pic_width_in_mbs_minus1
        final int picWidthInMbsMinus1 = reader.readUe();
        // pic_height_in_map_units_minus1
        final int picHeightInMapUnitsMinus1 = reader.readUe();
        // frame_mbs_only_flag
        final int frameMbsOnlyFlag = reader.readU1();
        // !frame_mbs_only_flag
        // 场编码
        if (frameMbsOnlyFlag == 0) {
            // mb_adaptive_frame_field_flag
            reader.readU1();
        }

        // direct_8x8_inference_flag
        reader.readU1();

        int frameCropLeftOffset = 0;
        int frameCropRightOffset = 0;
        int frameCropTopOffset = 0;
        int frameCropBottomOffset = 0;

        // frame_cropping_flag
        final int frameCroppingFlag = reader.readU1();
        if (frameCroppingFlag == 1) {
            // frame_crop_left_offset
            frameCropLeftOffset = reader.readUe();
            // frame_crop_right_offset
            frameCropRightOffset = reader.readUe();
            // frame_crop_top_offset
            frameCropTopOffset = reader.readUe();
            // frame_crop_bottom_offset
            frameCropBottomOffset = reader.readUe();
        }

        // vui_parameters_present_flag
        final int vuiParametersPresentFlag = reader.readU1();
        final int[] sarScale = {1};
        if (vuiParametersPresentFlag == 1) {
            vuiParameters(reader, sarScale);
        }

        final int width = (((picWidthInMbsMinus1 + 1) * 16) - frameCropLeftOffset * 2 - frameCropRightOffset * 2) * sarScale[0];

        final int height = ((2 - frameMbsOnlyFlag) * (picHeightInMapUnitsMinus1 + 1) * 16) - ((frameMbsOnlyFlag == 1 ? 2 : 4) * (frameCropTopOffset + frameCropBottomOffset));

        return new Sps()
                .setProfileIdc((byte) profileIdc)
                .setLevelIdc((byte) levelIdc)
                .setProfileCompat((byte) profileCompat)
                .setWidth(width)
                .setHeight(height);
    }

    /**
     * <pre>
     * {@code
     * scaling_list( scalingList, sizeOfScalingList, useDefaultScalingMatrixFlag ) {
     *     lastScale = 8
     *     nextScale = 8
     *     for( j = 0; j < sizeOfScalingList; j++ ) {
     *         if( nextScale != 0 ) {
     *             delta_scale // 0|1 se(v)
     *             nextScale = ( lastScale + delta_scale + 256 ) % 256
     *             useDefaultScalingMatrixFlag = ( j = = 0 && nextScale = = 0 )
     *         }
     *         scalingList[ j ] = ( nextScale = = 0 ) ? lastScale : nextScale
     *         lastScale = scalingList[ j ]
     *     }
     * }
     * }
     * </pre>
     *
     * @see <a href="https://www.itu.int/rec/T-REC-H.264">T-REC-H.264-202108-I!!PDF-E.pdf -- 7.3.2.1.1.1</a>
     */
    void scalingList(int sizeOfScalingList, BitStreamReader reader) {
        int lastScale = 8;
        int nextScale = 8;
        for (int j = 0; j < sizeOfScalingList; j++) {
            if (nextScale != 0) {
                // delta_scale
                // final int deltaScale = reader.readSe();
                final int deltaScale = reader.readSe();
                nextScale = (lastScale + deltaScale + 256) % 256;
            }
            lastScale = (nextScale == 0) ? lastScale : nextScale;
        }
    }

    /**
     * <pre>
     * {@code
     * vui_parameters( ) {
     *     aspect_ratio_info_present_flag // u(1)
     *     if( aspect_ratio_info_present_flag ) {
     *         aspect_ratio_idc // u(8)
     *         if( aspect_ratio_idc = = Extended_SAR ) {
     *              sar_width   // u(16)
     *              sar_height  // u(16)
     *         }
     *     }
     *     overscan_info_present_flag // u(1)
     *     if( overscan_info_present_flag )
     *          overscan_appropriate_flag // u(1)
     *     video_signal_type_present_flag // u(1)
     *     if( video_signal_type_present_flag ) {
     *         video_format   // u(3)
     *         video_full_range_flag // u(1)
     *         colour_description_present_flag // u(1)
     *         if( colour_description_present_flag ) {
     *             colour_primaries // u(8)
     *             transfer_characteristics // u(8)
     *             matrix_coefficients // u(8)
     *         }
     *     }
     *     chroma_loc_info_present_flag // u(1)
     *     if( chroma_loc_info_present_flag ) {
     *         chroma_sample_loc_type_top_field // ue(v)
     *         chroma_sample_loc_type_bottom_field // ue(v)
     *     }
     * }
     * }
     * </pre>
     *
     * @see <a href="https://www.itu.int/rec/T-REC-H.264">T-REC-H.264-202108-I!!PDF-E.pdf -- E.1.1 VUI parameters syntax</a>
     */
    private void vuiParameters(BitStreamReader reader, int[] sarScale) {
        // aspect_ratio_info_present_flag
        final int aspectRatioInfoPresentFlag = reader.readU1();
        if (aspectRatioInfoPresentFlag == 1) {
            // aspect_ratio_idc
            final int aspectRatioIdc = reader.readU8();
            final int[] ratioInfo = this.getAspectRatioIdcMapping(aspectRatioIdc, reader);
            if (ratioInfo != null) {
                sarScale[0] = ratioInfo[0] / ratioInfo[1];
            }
        }
        // overscan_info_present_flag
        final int overscanInfoPresentFlag = reader.readU1();
        if (overscanInfoPresentFlag == 1) {
            // overscan_appropriate_flag
            reader.readU1();
        }
        // video_signal_type_present_flag
        final int videoSignalTypePresentFlag = reader.readU1();
        if (videoSignalTypePresentFlag == 1) {
            // video_format
            reader.readU3();
            // video_full_range_flag
            reader.readU1();
            // colour_description_present_flag
            final int colourDescriptionPresentFlag = reader.readU1();
            if (colourDescriptionPresentFlag == 1) {
                // colour_primaries
                reader.readU8();
                // transfer_characteristics
                reader.readU8();
                // matrix_coefficients
                reader.readU8();
            }
        }

        // chroma_loc_info_present_flag
        final int chromaLocInfoPresentFlag = reader.readU1();
        if (chromaLocInfoPresentFlag == 1) {
            // chroma_sample_loc_type_top_field
            reader.readUe();
            // chroma_sample_loc_type_bottom_field
            reader.readUe();
        }
    }

    /**
     * @see <a href="https://www.itu.int/rec/T-REC-H.264">T-REC-H.264-202108-I!!PDF-E.pdf -- Table E-1 Meaning of sample aspect ratio indicator</a>
     */
    @Nullable
    private int[] getAspectRatioIdcMapping(int aspectRatioIdc, BitStreamReader reader) {
        if (aspectRatioIdc == 255) {
            // sar_width
            final int sarWidth = reader.readU16();
            // sar_height
            final int sarHeight = reader.readU16();
            return new int[]{sarWidth, sarHeight};
        }
        return ASPECT_RATIO_IDC_MAPPING.get(aspectRatioIdc);
    }

}
