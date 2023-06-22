/**
 * H.264 ---> ITU-T
 * <p>
 * AVC/MPEG ---> ISO-IEC
 * <p>
 * H.264裸流没有音频，没有时间戳。直接播放不太现实。
 * <p>
 * 要播放就要再封装一层: 加上时间戳等信息,比如 FLV, MP4, ...
 * <p>
 * H.264的封装格式有两种：
 * <ol>
 *     <li>avcC/avc1</li>
 *     <li>Annex-B： ITU-T 的附录 B 中出现的，所以叫做 Annex-B；SPS 和 PPS 被当做普通的 NALU 对待</li>
 * </ol>
 */
package io.github.hylexus.jt.jt1078.support.extension.h264;
