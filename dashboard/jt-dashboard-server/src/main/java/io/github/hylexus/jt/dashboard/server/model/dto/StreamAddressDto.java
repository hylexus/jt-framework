package io.github.hylexus.jt.dashboard.server.model.dto;

import io.github.hylexus.jt.dashboard.server.model.constants.ProtocolType;
import io.github.hylexus.jt.dashboard.server.model.constants.StreamType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

// import javax.validation.constraints.NotEmpty;
// import javax.validation.constraints.NotNull;
import java.time.Duration;

@Data
public class StreamAddressDto {
    // jt808 server instance
    @NotNull(message = "instanceId is null")
    @NotEmpty(message = "instanceId is empty")
    private String instanceId;

    private ProtocolType protocolType = ProtocolType.WEBSOCKET;
    private StreamType streamType = StreamType.MAIN_STREAM;

    @NotNull(message = "sim is null")
    @NotEmpty(message = "sim is empty")
    private String sim;

    @NotNull(message = "channelNumber is null")
    private Integer channelNumber;

    @NotNull(message = "dataType is null")
    private Integer dataType;

    private Duration timeout = Duration.ofSeconds(10);
}
