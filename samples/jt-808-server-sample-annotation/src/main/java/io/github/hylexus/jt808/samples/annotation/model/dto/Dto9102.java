package io.github.hylexus.jt808.samples.annotation.model.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class Dto9102 {

    @NotNull(message = "terminalId is null")
    @NotEmpty(message = "terminalId is empty")
    private String terminalId;

    @NotNull(message = "channelNumber is null")
    private Integer channelNumber;

    @NotNull(message = "command is null")
    private Integer command;

    @NotNull(message = "mediaTypeToClose is null")
    private Integer mediaTypeToClose;

    @NotNull(message = "streamType is null")
    private Integer streamType;
}
