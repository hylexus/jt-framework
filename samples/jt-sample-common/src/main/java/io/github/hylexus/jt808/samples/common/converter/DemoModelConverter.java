package io.github.hylexus.jt808.samples.common.converter;

import io.github.hylexus.jt808.samples.common.dto.Command9101Dto;
import io.github.hylexus.jt808.samples.common.dto.DemoVideoStreamSubscriberDto;

public class DemoModelConverter {

    public static Command9101Dto command9101Dto(DemoVideoStreamSubscriberDto params) {
        return new Command9101Dto()
                .setSim(params.getSim())
                .setJt1078ServerIp(params.getJt1078ServerIp())
                .setJt1078ServerPortTcp(params.getJt1078ServerPortTcp())
                .setJt1078ServerPortUdp(params.getJt1078ServerPortUdp())
                .setChannelNumber((int) params.getChannel())
                // 音视频
                .setDataType(params.getDataType())
                // 主码流/子码流
                .setStreamType(params.getStreamType());
    }
}
