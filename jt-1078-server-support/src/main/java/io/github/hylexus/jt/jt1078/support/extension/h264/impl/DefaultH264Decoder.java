package io.github.hylexus.jt.jt1078.support.extension.h264.impl;

import io.github.hylexus.jt.jt1078.spec.Jt1078Request;
import io.github.hylexus.jt.jt1078.spec.Jt1078RequestHeader;
import io.github.hylexus.jt.jt1078.support.extension.h264.H264Decoder;
import io.github.hylexus.jt.jt1078.support.extension.h264.H264Nalu;
import io.github.hylexus.jt.jt1078.support.extension.h264.H264NaluHeader;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

/**
 * 该实现中的状态机算法来自 <a href="https://github.com/samirkumardas/jmuxer/blob/579835d7d72796d3374e0c62929456e7202bcb12/src/parsers/h264.js#LL7C18-L7C18">github--jmuxer--h264.js</a>
 *
 * @see <a href="https://github.com/samirkumardas/jmuxer/blob/579835d7d72796d3374e0c62929456e7202bcb12/src/parsers/h264.js#LL7C18-L7C18">github--jmuxer--h264.js</a>
 * @see <a href="https://gitee.com/ldming/JT1078">https://gitee.com/ldming/JT1078</a>
 * @see <a href="https://gitee.com/matrixy/jtt1078-video-server">https://gitee.com/matrixy/jtt1078-video-server</a>
 */
public class DefaultH264Decoder implements H264Decoder {

    @Override
    public List<H264Nalu> decode(Jt1078Request request) {
        final List<H264Nalu> result = new ArrayList<>();
        final ByteBuf byteBuf = request.body();
        final int length = byteBuf.readableBytes();

        int i = 0;
        int state = 0;
        int sliceStartIndex = -1;

        while (i < length) {
            final byte value = byteBuf.getByte(i++);
            switch (state) {
                case 0: {
                    if (value == 0) {
                        state = 1;
                    }
                    break;
                }
                // [0]01,[0]001
                case 1: {
                    state = value == 0 ? 2 : 0;
                    break;
                }
                // [00]1,[00]01
                case 2: {
                    if (value == 1 && i < length) {
                        // match [001]
                        final ByteBuf data = byteBuf.slice(sliceStartIndex, i - state - sliceStartIndex - 1);
                        final H264Nalu nalu = createH264Nalu(request, data);
                        result.add(nalu);
                        sliceStartIndex = i;
                        state = 0;
                    } else if (value == 0) {
                        state = 3;
                    } else {
                        state = 0;
                    }
                    break;
                }
                // [000]1
                case 3: {
                    // match [0001]
                    if (value == 1 && i < length) {
                        if (sliceStartIndex >= 0) {
                            final ByteBuf data = byteBuf.slice(sliceStartIndex, i - state - sliceStartIndex - 1);
                            final H264Nalu nalu = createH264Nalu(request, data);
                            result.add(nalu);
                        }
                        sliceStartIndex = i;
                    }
                    state = 0;
                    break;
                }
                default: {
                    break;
                }
            }
        }

        if (sliceStartIndex >= 0) {
            final ByteBuf data = byteBuf.slice(sliceStartIndex, length - sliceStartIndex);
            result.add(createH264Nalu(request, data));
        }

        return result;
    }

    private static H264Nalu createH264Nalu(Jt1078Request request, ByteBuf data) {
        final H264NaluHeader h264NaluHeader = H264NaluHeader.of(data.getByte(0));
        final Jt1078RequestHeader header = request.header();
        return new DefaultH264Nalu(
                h264NaluHeader,
                // data.slice(1, data.readableBytes() - 1),
                data.slice(0, data.readableBytes()),
                header.dataType(),
                header.lastIFrameInterval().orElse(null),
                header.lastFrameInterval().orElse(null),
                header.timestamp().orElse(null)
        );
    }
}
