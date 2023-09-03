package io.github.hylexus.jt.dashboard.server.model.converter;

import io.github.hylexus.jt.dashboard.common.model.dto.jt808.DashboardCommand9101Dto;
import io.github.hylexus.jt.dashboard.server.model.dto.StreamAddressDto;
import io.github.hylexus.jt.dashboard.server.model.values.instance.Jt1078Instance;

public class DashboardModelConverter {
    public static DashboardCommand9101Dto convert(StreamAddressDto dto, Jt1078Instance instance) {
        return new DashboardCommand9101Dto()
                .setSim(dto.getSim())
                .setJt1078ServerIp(instance.getRegistration().getHost())
                // 暂时不支持 UDP 协议
                .setJt1078ServerPortUdp(0)
                .setJt1078ServerPortTcp(instance.getRegistration().getTcpPort())
                .setChannelNumber(dto.getChannelNumber())
                .setDataType(dto.getDataType())
                .setStreamType(dto.getStreamType().getValue())
                .setTimeout(dto.getTimeout());
    }
}
