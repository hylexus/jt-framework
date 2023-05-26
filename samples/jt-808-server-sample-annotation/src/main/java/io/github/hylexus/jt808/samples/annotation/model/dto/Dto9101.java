package io.github.hylexus.jt808.samples.annotation.model.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class Dto9101 {

    @NotNull(message = "terminalId is null")
    @NotEmpty(message = "terminalId is empty")
    private String terminalId;

    @NotNull(message = "serverIp is null")
    @NotEmpty(message = "serverIp is empty")
    private String serverIp;

    @NotNull(message = "serverPortTcp is null")
    private Integer serverPortTcp;

    @NotNull(message = "serverPortUdp is null")
    private Integer serverPortUdp;

    @NotNull(message = "channelNumber is null")
    private Integer channelNumber;

    @NotNull(message = "dataType is null")
    private Integer dataType;

    @NotNull(message = "streamType is null")
    private Integer streamType;
}
