/*
 * The H.264 state machine is adapted from jmuxer.
 * Copyright (c) 2018 Samir Das
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * Adapted for jt-framework by hylexus; see the upstream source link below.
 * The referenced JT1078 C# implementation also credits jmuxer. Its MIT notice
 * is retained in licenses/MIT-SmallChi-JT1078.txt; see third-party source notices.
 */

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
 * @see <a href="https://github.com/SmallChi/JT1078/blob/c4931a8ec37678fb5ecedcc749ffe3c9f4c890fe/src/JT1078.Protocol/H264/H264Decoder.cs">SmallChi/JT1078: reviewed H.264 parser snapshot</a>
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
