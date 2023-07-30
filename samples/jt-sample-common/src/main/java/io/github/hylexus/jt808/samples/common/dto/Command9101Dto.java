package io.github.hylexus.jt808.samples.common.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Duration;

@Data
@Accessors(chain = true)
public class Command9101Dto {

    @NotNull(message = "sim is null")
    @NotEmpty(message = "sim is empty")
    private String sim;

    @NotNull(message = "jt1078ServerIp is null")
    @NotEmpty(message = "jt1078ServerIp is empty")
    private String jt1078ServerIp;

    @NotNull(message = "jt1078ServerPortTcp is null")
    private Integer jt1078ServerPortTcp;

    @NotNull(message = "jt1078ServerPortUdp is null")
    private Integer jt1078ServerPortUdp;

    @NotNull(message = "channelNumber is null")
    private Integer channelNumber;

    @NotNull(message = "dataType is null")
    private Integer dataType;

    @NotNull(message = "streamType is null")
    private Integer streamType;

    private Duration timeout = Duration.ofSeconds(3);
}
