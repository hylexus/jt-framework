package io.github.hylexus.jt.demos.dashboard.boot3.model.converter;

import io.github.hylexus.jt.demos.common.model.dto.jt808.Command9101Dto;
import io.github.hylexus.jt.demos.dashboard.boot3.configuration.props.ServerMetadata;
import io.github.hylexus.jt.demos.dashboard.boot3.model.dto.StreamAddressDto;

public class SimpleConverter {
    public static Command9101Dto convert(StreamAddressDto dto, ServerMetadata.Jt1078ServerMetadata jt1078ServerInstance) {
        return new Command9101Dto()
                .setSim(dto.getSim())
                .setJt1078ServerIp(jt1078ServerInstance.getHost())
                // 暂时不支持 UDP 协议
                .setJt1078ServerPortUdp(0)
                .setJt1078ServerPortTcp(jt1078ServerInstance.getTcpPort())
                .setChannelNumber(dto.getChannelNumber())
                .setDataType(dto.getDataType())
                .setStreamType(dto.getStreamType().getValue())
                .setTimeout(dto.getTimeout());
    }
}
