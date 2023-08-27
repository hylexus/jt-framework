package io.github.hylexus.jt.dashboard.server.model.converter;

import io.github.hylexus.jt.dashboard.common.model.dto.jt808.Command9101Dto;
import io.github.hylexus.jt.dashboard.server.model.dto.StreamAddressDto;
import io.github.hylexus.jt.dashboard.server.model.dto.instance.Jt1078Registration;
import io.github.hylexus.jt.dashboard.server.model.values.instance.JtInstance;

public class SimpleConverter {
    public static Command9101Dto convert(StreamAddressDto dto, JtInstance jt1078ServerInstance) {
        return new Command9101Dto()
                .setSim(dto.getSim())
                .setJt1078ServerIp(((Jt1078Registration) jt1078ServerInstance.getRegistration()).getHost())
                // 暂时不支持 UDP 协议
                .setJt1078ServerPortUdp(0)
                .setJt1078ServerPortTcp(((Jt1078Registration) jt1078ServerInstance.getRegistration()).getTcpPort())
                .setChannelNumber(dto.getChannelNumber())
                .setDataType(dto.getDataType())
                .setStreamType(dto.getStreamType().getValue())
                .setTimeout(dto.getTimeout());
    }
}
