package io.github.hylexus.jt808.samples.common.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Duration;

@Data
public class Command9102Dto {

    @NotNull(message = "sim is null")
    @NotEmpty(message = "sim is empty")
    private String sim;

    @NotNull(message = "channelNumber is null")
    private Integer channelNumber;

    @NotNull(message = "command is null")
    private Integer command;

    @NotNull(message = "mediaTypeToClose is null")
    private Integer mediaTypeToClose;

    @NotNull(message = "streamType is null")
    private Integer streamType;

    private Duration timeout = Duration.ofSeconds(3);
}
